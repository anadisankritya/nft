package com.nft.app.dto;


public record UserRequest(
    String username,
    String email,
    String password,
    String referralCode,
    String otp
) {
}