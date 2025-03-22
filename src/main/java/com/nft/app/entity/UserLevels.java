package com.nft.app.entity;

import com.nft.app.dto.request.CreateInvestmentRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "investment_types")
public class UserLevels {
    @Id
    private String _id;
    private String name;
    private Integer seq;
    private List<String> images;
}