package com.nft.app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Document(collection = "userLevels")
public class UserLevel {
    @Id
    private String id;
    private String name;
    private Long seq;
    private String imageId;
    private boolean baseLevel;
    private String checkSum;
    private Integer startPrice;
    private Integer endPrice;
    private Integer startProfit;
    private Integer endProfit;
    private BigDecimal handlingFees;
    private List<String> rules;
}