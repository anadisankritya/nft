package com.nft.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  EMAIL_ALREADY_EXISTS("101", HttpStatus.CONFLICT, "User with same email already exists"),
  DUPLICATE_USER_CODE("102", HttpStatus.CONFLICT, "User with same user code already exists"),
  INVALID_OTP("103", HttpStatus.BAD_REQUEST, "Invalid OTP"),
  GENERIC_EXCEPTION("104", INTERNAL_SERVER_ERROR, "%s"),
  USER_NOT_FOUND("105", NOT_FOUND, "User not found"),
  USERNAME_ALREADY_EXISTS("106", HttpStatus.CONFLICT, "User with same username already exists"),
  REFERRAL_CODE_MANDATORY("107", HttpStatus.BAD_REQUEST, "Referral code is mandatory"),
  INVALID_REFERRAL_CODE("108", HttpStatus.UNPROCESSABLE_ENTITY, "Invalid referral code"),
  MOBILE_NO_ALREADY_EXISTS("109", HttpStatus.CONFLICT, "Mobile number already in use"),
  MISSING_HEADER("110", HttpStatus.BAD_REQUEST, "Missing Header Error"),
  MISSING_PARAMETER("111", BAD_REQUEST, "Missing Parameter Error"),
  INVALID_REQUEST("112", BAD_REQUEST, "Invalid request body"),
  URI_EXCEPTION("113", INTERNAL_SERVER_ERROR, "URI seems to be invalid"),
  INVALID_TOKEN("114", UNAUTHORIZED, "Unauthorized request. Invalid token"),
  OBJECT_MAPPING_FAILED("115", INTERNAL_SERVER_ERROR, "Object mapping failed"),
  WALLET_NOT_FOUND("116", INTERNAL_SERVER_ERROR, "Wallet linked to user not found"),
  MINIMUM_WITHDRAW_ERROR("117", BAD_REQUEST, "Minimum withdraw amount should be 50"),
  NEW_USER_WITHDRAW("118", BAD_REQUEST, "Cannot withdraw until 7 days of registration"),
  PENDING_WITHDRAW_REQUEST("119", BAD_REQUEST, "Previous withdraw request is already pending"),
  INSUFFICIENT_FUNDS("120", BAD_REQUEST, "Insufficient wallet amount"),
  INVALID_PASSWORD("121", UNAUTHORIZED, "Invalid password");


  private final String code;
  private final HttpStatus httpStatus;
  private final String displayMessage;

}
