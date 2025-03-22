package com.nft.app.dto.request;


import jakarta.validation.constraints.NotEmpty;

public record UserRequest(

    @NotEmpty(message = "username is required")
    String username,

    @NotEmpty(message = "email is required")
    String email,

    @NotEmpty(message = "password is required")
    String password,

    @NotEmpty(message = "phoneNo is required")
    String phoneNo,

    String referralCode,

    @NotEmpty(message = "emailOtp is required")
    String emailOtp,

    @NotEmpty(message = "smsOtp is required")
    String smsOtp
) {
}