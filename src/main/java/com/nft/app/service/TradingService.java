package com.nft.app.service;

import com.mongodb.DuplicateKeyException;
import com.nft.app.dto.request.BuyOrderRequest;
import com.nft.app.dto.request.ImageData;
import com.nft.app.dto.request.SellOrderRequest;
import com.nft.app.dto.response.*;
import com.nft.app.entity.AppConfig;
import com.nft.app.entity.SellTradingDetailsEntity;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TradingService {
    public static final String CURRENT_USER_BALANCE = "currentUserBalance";
    public static final String USER_PROFIT_WITHOUT_DEDUCTION = "userProfitWithoutDeduction";
    public static final String HANDLING_FEES = "handlingFees";
    public static final String USER_PROFIT_WITH_DEDUCTION = "userProfitWithDeduction";
    public static final String UPDATED_USER_BALANCE = "updatedUserBalance";
    public static final String PROFIT_SHARING = "PROFIT_SHARING";
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
    private final GridFsService gridFsService;


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
        ImageData image = nftDetails.getImage();
        tradingDetailsEntity.setNftImageId(image.getImageId());
        tradingDetailsEntity.setNftProfit(nftDetails.getProfit());
        tradingDetailsEntity.setNftBlockPeriod(nftDetails.getBlockPeriod());
        tradingDetailsEntity.setLevelHandlingFees(nftUserLevelResponse.getHandlingFees());
        tradingDetailsEntity.setNftId(nftDetails.getId());
        tradingDetailsEntity.setTradeStatus(TradeStatusEnum.IN_PROGRESS);
        Long blockPeriod = System.currentTimeMillis() + TimeUtil.convertDaysToMilliseconds(
                nftDetails.getBlockPeriod()
        );
        tradingDetailsEntity.setLevelId(nftDetails.getUserLevelId());
        tradingDetailsEntity.setSellBlockTill(blockPeriod);
        tradingDetailsEntity.setOperation(BUY);
        tradingDetailsEntity.setBreakupCreated(Boolean.FALSE);
        tradingDetailsEntity.setUserProfitShared(Boolean.FALSE);
        tradingDetailsEntity.setUserProfitBlocked(Boolean.TRUE);
        try {
            tradingDetailsEntity = tradingDetailsRepository.save(tradingDetailsEntity);
            walletService.updateWallet(email, -nftDetails.getBuyPrice(), "CUSTOMER_BUY_ORDER");
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
            throw new TradingException(ErrorCode.INVALID_ORDER_ID);
        }
        TradingDetailsEntity detailsEntity = tradingDetails.get();
        if (detailsEntity.getSellBlockTill() > System.currentTimeMillis()) {
            throw new TradingException(ErrorCode.ERR_ORDER_IS_NOT_READY_FRO_SELL);
        }
        detailsEntity.setOperation(SELL);
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
            SellTradingDetailsEntity sellTradingDetailsEntity = new SellTradingDetailsEntity();
            sellTradingDetailsEntity.setCreatedBy(userDetails.getUserCode());
            sellTradingDetailsEntity.setCreatedAt(System.currentTimeMillis());
            sellTradingDetailsEntity.setTradeId(detailsEntity.getId());
            sellTradingDetailsEntity.setOperation(PROFIT_SHARING);
            sellTradingDetailsEntity.setBreakupCreated(Boolean.FALSE);
            sellTradingDetailsEntity.setNftId(detailsEntity.getNftId());
            sellTradingDetailsEntity.setTeamProfitBlocked(Boolean.TRUE);
            sellTradingDetailsEntity.setTeamProfitShared(Boolean.FALSE);
            sellTradingDetailsRepository.save(sellTradingDetailsEntity);
            if (!appConfig.getBlockProfitSharing()) {
                walletService.updateWallet(
                        email, pd, "CUSTOMER_SELL_ORDER");
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

    public PageResponse<List<BuyOrderResponse>> getAllOngoingTask(int page, int size, String email) {
        UserDetails userDetails = userService.getUserDetails(email);
        Pageable pageable = PageRequest.of(page, size);
        Page<TradingDetailsEntity> tradingDetailsEntityPage = tradingDetailsRepository.findByOperationAndCreatedByAndSellBlockTillGreaterThan(
                "BUY", userDetails.getUserCode(), System.currentTimeMillis(), pageable
        );

        List<TradingDetailsEntity> tradingDetailsEntities = tradingDetailsEntityPage.getContent();
        List<BuyOrderResponse> buyOrderResponseList = new ArrayList<>();
        for (TradingDetailsEntity tradingDetails :  tradingDetailsEntities) {
            List<ImageData> imageData = gridFsService.getFileDetailsByIds(List.of(tradingDetails.getNftImageId()));
            boolean emptyList = Objects.isNull(imageData) || imageData.isEmpty();
            if (!emptyList)
                buyOrderResponseList.add(
                        new BuyOrderResponse(tradingDetails, imageData.getFirst())
                );
            else
                buyOrderResponseList.add(
                        new BuyOrderResponse(tradingDetails, null)
                );
        }

        return new PageResponse<List<BuyOrderResponse>>(
                tradingDetailsEntityPage.getTotalElements(),
                buyOrderResponseList
        );
    }

    public PageResponse<List<BuyOrderResponse>> getAllCompletedTask(int page, int size, String email) {
        UserDetails userDetails = userService.getUserDetails(email);
        Pageable pageable = PageRequest.of(page, size);
        Page<TradingDetailsEntity> tradingDetailsEntityPage = tradingDetailsRepository.findByOperationInAndCreatedByAndSellBlockTillLessThan(
                List.of("BUY", "SELL"), userDetails.getUserCode(), System.currentTimeMillis(), pageable
        );

        List<TradingDetailsEntity> tradingDetailsEntities = tradingDetailsEntityPage.getContent();

        List<BuyOrderResponse> buyOrderResponseList = tradingDetailsEntities.stream().
                map(trade -> {
                    List<ImageData> imageData = gridFsService.getFileDetailsByIds(List.of(trade.getNftImageId()));
                    boolean emptyList = Objects.isNull(imageData) || imageData.isEmpty();
                    if (!emptyList)
                        return new BuyOrderResponse(trade, imageData.getFirst());
                    else
                        return new BuyOrderResponse(trade, null);
                }).toList();
        return new PageResponse<List<BuyOrderResponse>>(
                tradingDetailsEntityPage.getTotalElements(),
                buyOrderResponseList
        );
    }
}
