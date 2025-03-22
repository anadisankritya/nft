package com.nft.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateInvestmentRequest {
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Allowed level is required")
    private String allowedLevel;

    private List<String> allowedLevels;
}