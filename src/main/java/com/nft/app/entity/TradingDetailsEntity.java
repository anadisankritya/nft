package com.nft.app.entity;

import com.nft.app.enums.TradeStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Document(collection = "tradingDetails")
public class TradingDetailsEntity {
    @Id
    private String id;
    private String nftId;
    private Double nftProfit;
    private Double nftBuyPrice;
    private Integer nftBlockPeriod;
    private BigDecimal levelHandlingFees;
    private String levelId;
    private String operation;
    private Long createdAt;
    private String createdBy;
    private Long sellBlockTill;
    private boolean userProfitShared;
    private boolean userProfitBlocked;
    private Map<String, Long> userProfitBreakup;
    private boolean breakupCreated;
    private TradeStatusEnum tradeStatus;
}