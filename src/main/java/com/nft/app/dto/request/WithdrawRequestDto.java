package com.nft.app.dto.request;

public record WithdrawRequestDto(
    Integer amount,
    String otp,
    String network,
    String walletAddress) {
}
