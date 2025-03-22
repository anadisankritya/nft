package com.nft.app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "userLevels")
public class NFTDetails {
    @Id
    private String id;
    private String name;
    private String imageId;
    private String checkSum;
    private String randomName;
    private String ownerName;
    private BigDecimal profit;
    private BigDecimal buyPrice;
    private Integer blockPeriod;
    private String category;
    private String InvestmentType;
    private String allowedLevel;
    private Boolean status;
    private LocalDateTime createdAt;
}