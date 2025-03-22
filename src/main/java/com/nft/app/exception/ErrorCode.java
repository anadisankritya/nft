package com.nft.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
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
  INVALID_REQUEST("112", BAD_REQUEST, "Invalid request"),
  URI_EXCEPTION("113", INTERNAL_SERVER_ERROR, "URI seems to be invalid"),
  INVALID_TOKEN("114", UNAUTHORIZED, "Unauthorized request. Invalid token"),
  OBJECT_MAPPING_FAILED("115", INTERNAL_SERVER_ERROR, "Object mapping failed"),
  INVALID_LEVEL_INSERTED("116", BAD_REQUEST, "Please add levels in comma separate eg: 1,2,3"),
  DUPLICATE_INVESTMENT("117", BAD_REQUEST, "Duplicate investment type."),
  CREATE_INVESTMENT_FAILED("118", BAD_REQUEST, "Create investment type failed."),
  INVALID_INVESTMENT_TYPE("119", NOT_FOUND, "Invalid investment type id"),
  UPDATE_INVESTMENT_FAILED("120", BAD_REQUEST, "Update investment type failed."),
  WALLET_NOT_FOUND("121", INTERNAL_SERVER_ERROR, "Wallet linked to user not found"),
  MINIMUM_WITHDRAW_ERROR("122", BAD_REQUEST, "Minimum withdraw amount should be 50"),
  NEW_USER_WITHDRAW("123", BAD_REQUEST, "Cannot withdraw until 7 days of registration"),
  PENDING_WITHDRAW_REQUEST("124", BAD_REQUEST, "Previous withdraw request is already pending"),
  INSUFFICIENT_FUNDS("125", BAD_REQUEST, "Insufficient wallet amount"),
  INVALID_PASSWORD("126", UNAUTHORIZED, "Invalid password"),
  DUPLICATE_USER_LEVEL("127", BAD_REQUEST, "Duplicate User level."),
  CREATE_USER_LEVEL_FAILED("128", BAD_REQUEST, "Create User level failed."),
  USER_LEVEL_NOT_FOUND("129", NOT_FOUND, "User level Not found"),
  DUPLICATE_USER_LEVEL_IMAGE("130", CONFLICT, "Duplicate image upload for user level"),
  TRANSACTION_ID_ALREADY_PRESENT("131", CONFLICT, "This transaction Id has already been used");

  private final String code;
  private final HttpStatus httpStatus;
  private final String displayMessage;

}
