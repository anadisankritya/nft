package com.nft.app.service;

import com.mongodb.DuplicateKeyException;
import com.nft.app.dto.request.BuyOrderRequest;
import com.nft.app.dto.request.SellOrderRequest;
import com.nft.app.dto.response.CreateNFTResponse;
import com.nft.app.dto.response.CreateUserLevelResponse;
import com.nft.app.dto.response.UserDetails;
import com.nft.app.entity.AppConfig;
import com.nft.app.entity.TradingDetailsEntity;
import com.nft.app.enums.TradeStatusEnum;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.TradingException;
import com.nft.app.repository.AppConfigRepository;
import com.nft.app.repository.SellTradingDetailsRepository;
import com.nft.app.repository.TradingDetailsRepository;
import com.nft.app.util.CommonUtil;
import com.nft.app.util.TimeUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TradingService {

  public static final String CURRENT_USER_BALANCE = "currentUserBalance";
  public static final String USER_PROFIT_WITHOUT_DEDUCTION = "userProfitWithoutDeduction";
  public static final String HANDLING_FEES = "handlingFees";
  public static final String USER_PROFIT_WITH_DEDUCTION = "userProfitWithDeduction";
  public static final String UPDATED_USER_BALANCE = "updatedUserBalance";
  private static AppConfig appConfig;
  public static final String BUY = "BUY";
  public static final String SELL = "SELL";
  private final UserService userService;
  private final TradingDetailsRepository tradingDetailsRepository;
  private final SellTradingDetailsRepository sellTradingDetailsRepository;
  private final NFTService nftService;
  private final UserLevelService userLevelService;
  private final WalletService walletService;
  private final AppConfigRepository appConfigRepository;


  @PostConstruct
  private void init() {
    List<AppConfig> appConfigList = appConfigRepository.findAll();
    if (!appConfigList.isEmpty()) {
      appConfig = appConfigList.getFirst();
    }
  }

  @Transactional
  public void createBuyOrder(BuyOrderRequest buyOrderRequest, String email) {
    UserDetails userDetails = userService.getUserDetails(email);
    CreateNFTResponse nftDetails = nftService.getNFTDetailsById(buyOrderRequest.getNftId());
    Optional<TradingDetailsEntity> duplicateTradingDetails = tradingDetailsRepository.findByCreatedByAndNftId(
        userDetails.getUserCode(), buyOrderRequest.getNftId());
    if (duplicateTradingDetails.isPresent()) {
      throw new TradingException(ErrorCode.DUPLICATE_BUY_ORDER);
    }
    String userLevelId = nftDetails.getUserLevelId();
    CreateUserLevelResponse nftUserLevelResponse = userLevelService.getUserLevelById(userLevelId);
    CreateUserLevelResponse loginUserLevelResponse = userLevelService.getUserLevelById(userDetails.getLevelId());
    if (nftUserLevelResponse.getSeq() > loginUserLevelResponse.getSeq())
      throw new TradingException(ErrorCode.INVALID_USER_LEVEL);

    UserDetails.WalletDetails walletDetails = userDetails.getWalletDetails();
    if (nftDetails.getBuyPrice() > walletDetails.getWalletBalance())
      throw new TradingException(ErrorCode.INVALID_WALLET_AMOUNT);

    TradingDetailsEntity tradingDetailsEntity = new TradingDetailsEntity();
    tradingDetailsEntity.setCreatedBy(
        userDetails.getUserCode()
    );
    tradingDetailsEntity.setCreatedAt(System.currentTimeMillis());
    tradingDetailsEntity.setNftBuyPrice(nftDetails.getBuyPrice());
    tradingDetailsEntity.setNftProfit(nftDetails.getProfit());
    tradingDetailsEntity.setNftBlockPeriod(nftDetails.getBlockPeriod());
    tradingDetailsEntity.setLevelHandlingFees(nftUserLevelResponse.getHandlingFees());
    tradingDetailsEntity.setNftId(nftDetails.getId());
    tradingDetailsEntity.setTradeStatus(TradeStatusEnum.IN_PROGRESS);
    Long blockPeriod = System.currentTimeMillis() + TimeUtil.convertDaysToMilliseconds(
        nftDetails.getBlockPeriod()
    );
    tradingDetailsEntity.setSellBlockTill(blockPeriod);
    tradingDetailsEntity.setOperation(BUY);
    tradingDetailsEntity.setBreakupCreated(Boolean.FALSE);
    tradingDetailsEntity.setUserProfitShared(Boolean.FALSE);
    tradingDetailsEntity.setUserProfitBlocked(Boolean.TRUE);
    try {
      tradingDetailsEntity = tradingDetailsRepository.save(tradingDetailsEntity);
      walletService.updateWallet(
          email, -nftDetails.getBuyPrice(), "BUY");
    } catch (DuplicateKeyException e) {
      throw new TradingException(ErrorCode.DUPLICATE_BUY_ORDER);
    } catch (Exception e) {
      tradingDetailsRepository.delete(tradingDetailsEntity);
      throw new TradingException(ErrorCode.TRADING_BUY_ORDER_FAILED);
    }
  }

  public void createSellOrder(SellOrderRequest sellOrderRequest, String email) {
    UserDetails userDetails = userService.getUserDetails(email);

    Optional<TradingDetailsEntity> tradingDetails = tradingDetailsRepository.findByCreatedByAndIdAndOperation(
        userDetails.getUserCode(), sellOrderRequest.getOrderId(), BUY);
    if (tradingDetails.isEmpty()) {
      throw new TradingException(ErrorCode.INVALID_REQUEST);
    }
    TradingDetailsEntity detailsEntity = tradingDetails.get();
    if (detailsEntity.getSellBlockTill() > System.currentTimeMillis()) {
      throw new TradingException(ErrorCode.INVALID_REQUEST);
    }
    detailsEntity.setOperation(SELL);
    CreateNFTResponse nftDetails = nftService.getNFTDetailsById(detailsEntity.getNftId());
    detailsEntity.setTradeStatus(TradeStatusEnum.PENDING);
    Map<String, Double> breakup = new HashMap<>();
    Double walletBalance = userDetails.getWalletDetails().getWalletBalance();
    breakup.put(CURRENT_USER_BALANCE, walletBalance);
    double profitBalance = CommonUtil.calculateProfit(
        detailsEntity.getNftBuyPrice(), detailsEntity.getNftProfit()
    );
    double charges = CommonUtil.calculateProfit(
        profitBalance, detailsEntity.getLevelHandlingFees().doubleValue()
    );
    breakup.put(USER_PROFIT_WITHOUT_DEDUCTION, profitBalance);
    breakup.put(HANDLING_FEES, charges);
    Double pd = profitBalance - charges;
    breakup.put(USER_PROFIT_WITH_DEDUCTION, pd);
    double newBalance = (walletBalance + pd);
    breakup.put(UPDATED_USER_BALANCE, newBalance);
    detailsEntity.setBreakupCreated(Boolean.TRUE);
    detailsEntity.setUserProfitBreakup(breakup);
    try {
      if (!appConfig.getBlockProfitSharing()) {
        walletService.updateWallet(
            email, pd, "SELL");
        detailsEntity.setUserProfitBlocked(Boolean.FALSE);
        detailsEntity.setTradeStatus(TradeStatusEnum.SUCCESS);
        detailsEntity.setUserProfitShared(Boolean.TRUE);
      }
      tradingDetailsRepository.save(detailsEntity);
    } catch (DuplicateKeyException e) {
      throw new TradingException(ErrorCode.DUPLICATE_BUY_ORDER);
    } catch (Exception e) {
      throw new TradingException(ErrorCode.GENERIC_EXCEPTION);
    }
  }
}
