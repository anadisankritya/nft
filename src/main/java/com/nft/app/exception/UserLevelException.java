package com.nft.app.exception;

public class UserLevelException extends NftException {
    public UserLevelException(ErrorCode errorCode) {
        super(errorCode);
    }
}
