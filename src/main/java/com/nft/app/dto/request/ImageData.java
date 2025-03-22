package com.nft.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "image is required")
    private String image;
    @NotBlank(message = "content type is required")
    private String contentType;
}