package com.nft.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  EMAIL_ALREADY_EXISTS("101", HttpStatus.NOT_ACCEPTABLE, "User with same email already exists"),
  DUPLICATE_USER_CODE("102", HttpStatus.CONFLICT, "User with same user code already exists"),
  INVALID_OTP("103", HttpStatus.BAD_REQUEST, "Invalid OTP"),
  GENERIC_EXCEPTION("104", INTERNAL_SERVER_ERROR, "%s"),
  USER_NOT_FOUND("105", NOT_FOUND, "User not found"),
  USERNAME_ALREADY_EXISTS("106", HttpStatus.CONFLICT, "User with same username already exists"),
  REFERRAL_CODE_MANDATORY("107", HttpStatus.BAD_REQUEST, "Referral code is mandatory"),
  INVALID_REFERRAL_CODE("108", HttpStatus.UNPROCESSABLE_ENTITY, "Invalid referral code"),
  MOBILE_NO_ALREADY_EXISTS("109", HttpStatus.CONFLICT, "Mobile number already in use");


  private final String code;
  private final HttpStatus httpStatus;
  private final String displayMessage;

}
