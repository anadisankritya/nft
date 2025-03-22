package com.nft.app.exception;

public class SequenceGeneratorException extends NftException {
    public SequenceGeneratorException(ErrorCode errorCode) {
        super(errorCode);
    }
}
