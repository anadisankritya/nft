package com.nft.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  EMAIL_ALREADY_EXISTS("101", HttpStatus.NOT_ACCEPTABLE, "User with same email already exists"),
  DUPLICATE_USER_CODE("102", HttpStatus.CONFLICT, "User with same user code already exists"),
  INVALID_OTP("103", HttpStatus.BAD_REQUEST, "Invalid OTP");


  private final String code;
  private final HttpStatus httpStatus;
  private final String displayMessage;

}
