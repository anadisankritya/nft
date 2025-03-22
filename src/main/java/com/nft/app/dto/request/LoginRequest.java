package com.nft.app.dto.request;

public record LoginRequest(
    String email,
    String password,
    String otp
) {

}