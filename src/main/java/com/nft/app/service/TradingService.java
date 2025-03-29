package com.nft.app.service;

import com.mongodb.DuplicateKeyException;
import com.nft.app.dto.request.BuyOrderDto;
import com.nft.app.dto.response.CreateNFTResponse;
import com.nft.app.dto.response.CreateUserLevelResponse;
import com.nft.app.dto.response.UserDetails;
import com.nft.app.entity.TradingDetailsEntity;
import com.nft.app.enums.TradeStatusEnum;
import com.nft.app.exception.TradingException;
import com.nft.app.exception.ErrorCode;
import com.nft.app.repository.TradingDetailsRepository;
import com.nft.app.util.TimeUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TradingService {

    private final UserService userService;
    private final TradingDetailsRepository tradingDetailsRepository;
    private final NFTService nftService;
    private final UserLevelService userLevelService;
    private final WalletService walletService;

    public void createBuyOrder(BuyOrderDto buyOrderDto, String email) {
        UserDetails userDetails = userService.getUserDetails(email);
        CreateNFTResponse nftDetails = nftService.getNFTDetailsById(buyOrderDto.getNftId());
        Optional<TradingDetailsEntity> duplicateTradingDetails = tradingDetailsRepository.findByCreatedByAndNftId(
                userDetails.getUserCode(), buyOrderDto.getNftId());
        if (duplicateTradingDetails.isPresent()) {
            throw new TradingException(ErrorCode.DUPLICATE_BUY_ORDER);
        }
        String userLevelId = nftDetails.getUserLevelId();
        CreateUserLevelResponse userLevelResponse =  userLevelService.getUserLevelById(userLevelId);
        if (userDetails.getLevel() > userLevelResponse.getSeq())
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
        tradingDetailsEntity.setLevelHandlingFees();
        tradingDetailsEntity.setNftId(nftDetails.getId());
        tradingDetailsEntity.setTradeStatus(TradeStatusEnum.IN_PROGRESS);
        Long blockPeriod = System.currentTimeMillis() + TimeUtil.convertDaysToMilliseconds(
                nftDetails.getBlockPeriod()
        );
        tradingDetailsEntity.setSellBlockTill(blockPeriod);
        tradingDetailsEntity.setOperation("BUY");
        tradingDetailsEntity.setBreakupCreated(Boolean.FALSE);
        tradingDetailsEntity.setBreakupCreated(Boolean.FALSE);
        tradingDetailsEntity.setUserProfitShared(Boolean.FALSE);
        tradingDetailsRepository.save(tradingDetailsEntity);
        walletService.updateWallet(
                email, Integer.valueOf(String.valueOf(nftDetails.getBuyPrice()))
        );
    }
}
