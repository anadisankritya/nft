package com.nft.app.exception;

public class TradingException extends NftException {
    public TradingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
