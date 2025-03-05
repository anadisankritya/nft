package com.nft.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@Getter
@AllArgsConstructor
public enum ErrorCode {
  OPEN_SEARCH_ADD_FAILED("8401", INTERNAL_SERVER_ERROR, "Error while adding data to OpenSearch Index");

  private final String code;
  private final HttpStatus httpStatus;
  private final String displayMessage;

}
