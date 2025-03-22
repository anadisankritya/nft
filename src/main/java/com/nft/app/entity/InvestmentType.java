package com.nft.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "investment_types")
public class InvestmentType {
    @Id
    private String _id;
    private String name;
    private Map<String, String> metadata; // Dynamic key-value pairs for metadata
}