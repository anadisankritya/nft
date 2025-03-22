package com.nft.app.entity;

import com.nft.app.dto.request.CreateInvestmentRequest;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "investmentTypes")
@NoArgsConstructor
public class InvestmentType {
    @Id
    private String id;
    private String name;
    private List<String> allowedLevels;

    public InvestmentType(CreateInvestmentRequest createInvestmentRequest) {
        this.name = createInvestmentRequest.getName();
        this.allowedLevels = createInvestmentRequest.getAllowedLevels();
    }
}