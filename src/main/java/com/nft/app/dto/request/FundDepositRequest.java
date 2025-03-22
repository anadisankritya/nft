package com.nft.app.dto.request;

public record FundDepositRequest(
    Integer amount,
    String transactionId
) {

}
