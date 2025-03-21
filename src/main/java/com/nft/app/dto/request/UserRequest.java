package com.nft.app.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserRequest(

    @NotEmpty(message = "username is required")
    String username,

    @NotNull(message = "email is required")
    String email,

    @NotNull(message = "password is required")
    String password,

    @NotNull(message = "phoneNo is required")
    String phoneNo,

    String referralCode,

    @NotNull(message = "emailOtp is required")
    String emailOtp,

    @NotNull(message = "smsOtp is required")
    String smsOtp
) {
}