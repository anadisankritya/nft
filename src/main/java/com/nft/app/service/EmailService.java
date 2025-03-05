package com.nft.app.service;

import com.nft.app.entity.EmailOtp;
import com.nft.app.repository.EmailOtpRepository;
import com.nft.app.util.OtpGenerator;
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
  private final EmailOtpRepository emailOtpRepository;

  public void sendEmailOtp(String toEmail) {
    String otp = OtpGenerator.generateSixDigitOtp();
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(toEmail);
      message.setSubject("OTP Verification Code");
      message.setText("Your OTP code is: " + otp);
      mailSender.send(message);
    } catch (Exception e) {
      log.error("Exception in sendEmailOtp", e);
    }
    EmailOtp emailOtp = new EmailOtp(toEmail, otp);
    emailOtpRepository.save(emailOtp);
  }
}
