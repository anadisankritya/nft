package com.nft.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateNFTRequest {
    private String name;
    private ImageData image;
    private String ownerName;
    private Double profit;
    private Double buyPrice;
    private Integer blockPeriod; // days
    private String category;
    private String investmentType;
    private String allowedLevel;
    private Boolean status;
}
