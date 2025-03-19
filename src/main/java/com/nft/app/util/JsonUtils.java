package com.nft.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JsonUtils {
  private final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> T convertJsonToObject(String json, Class<T> targetType) {
    try {
      return objectMapper.readValue(json, targetType);
    } catch (JsonProcessingException e) {
      log.info("Object mapping failed", e);
      throw new NftException(ErrorCode.OBJECT_MAPPING_FAILED);
    }
  }

  public static <T> String convertObjectToJson(T object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.info("Object mapping failed", e);
      throw new NftException(ErrorCode.OBJECT_MAPPING_FAILED);
    }
  }
}
