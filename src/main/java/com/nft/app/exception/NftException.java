package com.nft.app.exception;

import lombok.Getter;

@Getter
public class NftException extends RuntimeException {

  private final ErrorCode errorCode;

  public NftException(ErrorCode errorCode) {
    super(errorCode.getDisplayMessage());
    this.errorCode = errorCode;
  }

}
