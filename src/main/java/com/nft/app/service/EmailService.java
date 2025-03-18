package com.nft.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  public void sendEmailOtp(String email, String otp) {
    log.info("inside EmailService::sendEmailOtp for email - {}", email);
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(email);
      message.setSubject("OTP Verification Code");
      message.setText("Your OTP code is: " + otp);
      mailSender.send(message);
    } catch (Exception e) {
      log.error("Exception in sendEmailOtp", e);
      throw e;
    }
  }


}
