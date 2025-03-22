package com.nft.app.dto;

import lombok.Data;

import java.util.Map;

@Data
public class InvestmentTypeDto {
    private String _id;
    private String name;
    private Map<String, String> metadata;
}