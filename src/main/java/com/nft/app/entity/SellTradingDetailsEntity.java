package com.nft.app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Document(collection = "sellTradingDetails")
public class SellTradingDetailsEntity {
    @Id
    private String id;
    private String tradeId;
    private String nftId;
    private String operation;
    private Long createdAt;
    private Long createdBy;
    private boolean teamProfitShared;
    private boolean teamProfitBlocked;
    private Map<String, Long> teamProfitBreakup;
    private boolean breakupCreated;
}