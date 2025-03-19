package com.nft.app.service;

import com.nft.app.constant.AppConstants;
import com.nft.app.entity.OtpDetails;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import com.nft.app.repository.OtpRepository;
import com.nft.app.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

  private final OtpRepository otpRepository;
  private final EmailService emailService;
  private final SmsService smsService;

  public void sendOtp(String to, String type) {
    log.info("inside OtpService::sendOtp for  - {}", to);

    String otp = OtpGenerator.generateSixDigitOtp();
    switch (type) {
      case AppConstants.EMAIL -> emailService.sendEmailOtp(to, otp);
      case AppConstants.MOBILE -> smsService.sendMobileOtp(to, otp);
    }
    saveOtp(to, otp, type);
    log.info("Otp sent {} to {}", otp, to);
  }

  private void saveOtp(String key, String otp, String type) {
    OtpDetails otpDetails = new OtpDetails(key, otp, type);

    Optional<OtpDetails> otpOptional = otpRepository.findByTypeAndKey(type, key);
    if (otpOptional.isPresent()) {
      otpDetails = otpOptional.get();
      otpDetails.setOtp(otp);
    }
    otpRepository.save(otpDetails);
  }

  public void verifyOtp(String key, String otp, String type) {
    log.info("inside OtpService::verifyOtp for key - {}", key);
    Optional<OtpDetails> emailOtpOptional = otpRepository.findByTypeAndKey(type, key);
    if (emailOtpOptional.isPresent()) {
      OtpDetails emailOtpDetails = emailOtpOptional.get();
      if (emailOtpDetails.getOtp().equals(otp)) {
        log.info("Otp verified for key - {}", key);
        return;
      }
    }
    throw new NftException(ErrorCode.INVALID_OTP);
  }

  public boolean checkOtpAlreadySent(String key, String type) {
    Optional<OtpDetails> otpDetails = otpRepository.findByTypeAndKey(type, key);
    return otpDetails.map(
        details -> details.getUpdatedDate().isAfter(LocalDateTime.now().minusMinutes(15L))
    ).orElse(false);
  }
}
