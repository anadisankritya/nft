package com.nft.app.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nft.app.entity.UserLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateInvestmentRequest {

    private String id;
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Levels list cannot be null")
    @Size(min = 1, message = "Levels list must > 1")
    private List<String> levels;

    @JsonIgnore
    private List<UserLevel> userLevels;
}