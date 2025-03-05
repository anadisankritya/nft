package com.nft.app.exception;

public class UserCodeException extends NftException{

  public UserCodeException(ErrorCode errorCode) {
    super(errorCode);
  }
}
