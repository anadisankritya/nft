package com.nft.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserLevelRequest {
    private String id;
    @NotBlank(message = "Name is required")
    private String name;
    private ImageData image;
    @NotBlank(message = "content type is required")
    private String contentType;
    @NotNull(message = "base level is required")
    private Boolean baseLevel;
}