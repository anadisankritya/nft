package com.nft.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

  @Value("${sms.api.url}")
  private String smsApiUrl;

  @Value("${sms.api.key}")
  private String apiKey;

  @Value("${sms.sender}")
  private String sender;

  private final RestTemplate restTemplate;

  private void sendSms(String message, String numbers) {
    try {
      // Construct the request data
      String data = String.format("apikey=%s&message=%s&sender=%s&numbers=%s",
          apiKey, message, sender, numbers);

      // Set headers
      HttpHeaders headers = new HttpHeaders();
      headers.set("Content-Type", "application/x-www-form-urlencoded");

      HttpEntity<String> entity = new HttpEntity<>(data, headers);

      // Make the POST request
      ResponseEntity<String> response = restTemplate.exchange(
          smsApiUrl, HttpMethod.POST, entity, String.class);

      log.info("SMS sent successfully: {}", response.getBody());
    } catch (Exception e) {
      log.error("Error sending SMS: ", e);
      throw e;
    }
  }

  public void sendMobileOtp(String toNumber, String otp) {
    String otpMessage = "Your NFT otp is " + otp;
    sendSms(otpMessage, toNumber);
  }
}
