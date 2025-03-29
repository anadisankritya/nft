package com.nft.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotEmpty(message = "Email is required")
    @Email(message = "Email format invalid")
    String email,

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password,
    String otp
) {
}