package com.nft.app.service;

import com.nft.app.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  public void sendOtpEmail(String toEmail) {
    String otp = OtpGenerator.generateSixDigitOtp();
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject("OTP Verification Code");
    message.setText("Your OTP code is: " + otp);
    mailSender.send(message);
  }
}
