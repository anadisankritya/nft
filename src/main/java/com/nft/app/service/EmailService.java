package com.nft.app.service;

import com.nft.app.entity.EmailOtp;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import com.nft.app.repository.EmailOtpRepository;
import com.nft.app.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final EmailOtpRepository emailOtpRepository;

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

  public void verifyOtp(String email, String otp) {
    log.info("inside EmailService::verifyOtp for email - {}", email);
    Optional<EmailOtp> emailOtpOptional = emailOtpRepository.findByEmail(email);
    if (emailOtpOptional.isPresent()) {
      EmailOtp emailOtp = emailOtpOptional.get();
      if (emailOtp.getOtp().equals(otp)) {
        log.info("Otp verified for email - {}", email);
        return;
      }
    }
    throw new NftException(ErrorCode.INVALID_OTP);
  }


}
