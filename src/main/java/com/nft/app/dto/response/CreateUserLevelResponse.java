package com.nft.app.dto.response;

import com.nft.app.dto.request.ImageData;
import com.nft.app.entity.UserLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserLevelResponse {
    private String id;
    private String name;
    private Long seq;
    private ImageData imageData;
    private Boolean baseLevel;
    private Integer startPrice;
    private Integer endPrice;
    private Integer startProfit;
    private Integer endProfit;
    private BigDecimal handlingFees;


    public CreateUserLevelResponse(UserLevel userLevel, ImageData imageData) {
        this.id = userLevel.getId();
        this.name = userLevel.getName();
        this.seq = userLevel.getSeq();
        this.imageData = imageData;
        this.baseLevel = userLevel.isBaseLevel();
        this.startPrice = userLevel.getStartPrice();
        this.endPrice = userLevel.getEndPrice();
        this.startProfit = userLevel.getStartProfit();
        this.endProfit = userLevel.getEndProfit();
        this.handlingFees = userLevel.getHandlingFees();
    }
}