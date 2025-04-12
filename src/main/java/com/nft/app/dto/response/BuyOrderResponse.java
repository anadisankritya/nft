package com.nft.app.dto.response;

import com.nft.app.dto.request.ImageData;
import com.nft.app.entity.TradingDetailsEntity;
import com.nft.app.enums.TradeStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyOrderResponse {
    private Long sellBlockTill;
    private ImageData imageData;
    private String orderId;
    private String operation;
    private TradeStatusEnum tradeStatus;
    private String nftId;
    private boolean userProfitShared;

    public BuyOrderResponse(TradingDetailsEntity tradingDetailsEntity, ImageData imageData) {
        sellBlockTill = tradingDetailsEntity.getSellBlockTill();
        orderId = tradingDetailsEntity.getId();
        this.imageData = imageData;
        this.operation = tradingDetailsEntity.getOperation();
        this.tradeStatus = tradingDetailsEntity.getTradeStatus();
        this.nftId = tradingDetailsEntity.getNftId();
        this.userProfitShared = tradingDetailsEntity.isUserProfitShared();
    }
}
