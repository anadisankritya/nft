package com.nft.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nft.app.exception.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NftResponse<T>(
    String responseCode,
    String responseMessage,
    String displayMessage,
    T data
) {


  public NftResponse(ErrorCode errorCode) {
    this(errorCode, null);
  }

  public NftResponse(ErrorCode errorCode, T data) {
    this(errorCode.getCode(), errorCode.name(), errorCode.getDisplayMessage(), data);
  }

  public NftResponse(String displayMessage, T data) {
    this("200", "SUCCESS", displayMessage, data);
  }

  public NftResponse(String statusCode, String displayMessage, T data) {
    this("200", "SUCCESS", displayMessage, data);
  }

  public NftResponse(T data) {
    this("", data);
  }

  public NftResponse(Exception exception, ErrorCode errorCode) {
    this(errorCode.getCode(), errorCode.name(), exception.getMessage(), null);
  }

}