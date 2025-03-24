package com.nft.app.dto.response;

import com.nft.app.dto.request.ImageData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CreateNFTResponse {
    private String id;
    private String name;
    private ImageData image;
    private String checkSum;
    private String randomName;
    private String ownerName;
    private BigDecimal profit;
    private BigDecimal buyPrice;
    private Integer blockPeriod;
    private String category;
    private String InvestmentType;
    private String allowedLevel;
    private String status;
}
