package com.nft.app.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "nftDetails")
public class NFTDetails {
    @Id
    private String id;
    private String name;
    private String nftCode;
    private String imageId;
    private String checkSum;
    private String ownerName;
    private Double profit;
    private Double buyPrice;
    private Integer blockPeriod;
    private String category;
    private String InvestmentType;
    private String allowedLevel;
    private Boolean status;
    private LocalDateTime createdAt;
}