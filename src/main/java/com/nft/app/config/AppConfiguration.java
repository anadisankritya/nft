package com.nft.app.config;

import com.nft.app.entity.AppConfig;
import com.nft.app.repository.AppConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

  private final AppConfigRepository appConfigRepository;

  @PostConstruct
  public void init() {
    AppConfig appConfig = new AppConfig();
    appConfig.setOtpRequired(false);
    appConfig.setReferralCodeMandatory(false);
    appConfig.setMinWithdrawDays(7);
    appConfigRepository.deleteAll();
    appConfigRepository.save(appConfig);
  }


  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

}
