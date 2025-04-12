package com.nft.app.exception;

import com.nft.app.dto.NftResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class NftControllerAdvice {

  @ExceptionHandler(NftException.class)
  public ResponseEntity<NftResponse<Object>> handleDmsException(NftException ex) {
    log.error("NftException", ex);
    ErrorCode errorCode = ex.getErrorCode();
    NftResponse<Object> objectNftResponse = new NftResponse<>(ex, errorCode);
    return new ResponseEntity<>(objectNftResponse, errorCode.getHttpStatus());
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<NftResponse<Object>> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
    log.error("MissingRequestHeaderException", ex);
    ErrorCode missingHeaderError = ErrorCode.MISSING_HEADER;
    NftResponse<Object> objectNftResponse = new NftResponse<>(missingHeaderError);
    return new ResponseEntity<>(objectNftResponse, missingHeaderError.getHttpStatus());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<NftResponse<Object>> handleDmsWriteFallBackException(MissingServletRequestParameterException ex) {
    log.error("MissingServletRequestParameterException", ex);
    ErrorCode missingParameterError = ErrorCode.MISSING_PARAMETER;
    NftResponse<Object> objectNftResponse = new NftResponse<>(missingParameterError);
    return new ResponseEntity<>(objectNftResponse, missingParameterError.getHttpStatus());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<NftResponse<Object>> handleHttpMessageNotReadableException(Exception ex) {
    log.error("HttpMessageNotReadableException", ex);
    ErrorCode invalidRequest = ErrorCode.INVALID_REQUEST;
    NftResponse<Object> objectNftResponse = new NftResponse<>(invalidRequest);
    return new ResponseEntity<>(objectNftResponse, invalidRequest.getHttpStatus());

  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<NftResponse<Object>> handleNoResourceFoundException(Exception ex) {
    log.error("NoResourceFoundException", ex);
    ErrorCode uriException = ErrorCode.URI_EXCEPTION;
    NftResponse<Object> objectNftResponse = new NftResponse<>(uriException);
    return new ResponseEntity<>(objectNftResponse, uriException.getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<NftResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    log.error("MethodArgumentNotValidException", ex);
    ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
    String exceptionMessage = ex.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
    String displayMessage;
    if (StringUtils.hasLength(exceptionMessage))
      displayMessage = exceptionMessage;
    else
      displayMessage = errorCode.getDisplayMessage();
    NftResponse<Object> dmsResponse = new NftResponse<>(errorCode.getCode(), errorCode.name(), displayMessage, null);
    return new ResponseEntity<>(dmsResponse, errorCode.getHttpStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<NftResponse<Object>> handleException(Exception ex) {
    log.error("Exception", ex);
    ErrorCode genericException = ErrorCode.GENERIC_EXCEPTION;
    NftResponse<Object> objectNftResponse = new NftResponse<>(ex, genericException);
    return new ResponseEntity<>(objectNftResponse, genericException.getHttpStatus());
  }
}
