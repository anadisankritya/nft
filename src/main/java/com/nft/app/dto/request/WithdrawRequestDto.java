package com.nft.app.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record WithdrawRequestDto(
    @NotEmpty(message = "Amount is required")
    @Min(value = 50, message = "Min amount should be 50")
    @Max(value = 999, message = "Max amount should be 999")
    Integer amount,

    @NotEmpty(message = "Otp is required")
    String otp,

    @NotEmpty(message = "Network is required")
    String network,

    @NotEmpty(message = "Wallet address is required")
    String walletAddress) {
}
