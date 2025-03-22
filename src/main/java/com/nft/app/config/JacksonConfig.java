package com.nft.app.config;

import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    StreamReadConstraints constraints = StreamReadConstraints.builder().maxStringLength(Integer.MAX_VALUE).build();
    objectMapper.getFactory().setStreamReadConstraints(constraints);
    return objectMapper;
  }
}
