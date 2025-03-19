package com.nft.app.dto;


public record UserRequest(
    String username,
    String email,
    String password,
    String phoneNo,
    String referralCode,
    String emailOtp,
    String smsOtp
) {
}