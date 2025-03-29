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
    private Double profit;
    private Double buyPrice;
    private Integer blockPeriod;
    private String operation;
    private Long createdAt;
    private Long createdBy;
    private Long sellBlockTill;
    private boolean userProfitShared;
    private boolean userProfitBlocked;
    private Map<String, Long> userProfitBreakup;
    private boolean breakupCreated;
    private TradeStatusEnum tradeStatusEnum;
}