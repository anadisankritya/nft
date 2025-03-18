package com.nft.app.service;

import com.nft.app.dto.UserDetailsResponse;
import com.nft.app.dto.UserRequest;
import com.nft.app.entity.Otp;
import com.nft.app.entity.User;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import com.nft.app.exception.UserCodeException;
import com.nft.app.repository.OtpRepository;
import com.nft.app.repository.UserRepository;
import com.nft.app.util.AlphabeticalCodeGenerator;
import com.nft.app.util.JwtUtil;
import com.nft.app.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  public static final String EMAIL = "EMAIL";
  public static final String MOBILE = "MOBILE";
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final EmailService emailService;
  private final OtpRepository otpRepository;
  private final SmsService smsService;

  public void sendEmailOtp(String email) {
    log.info("inside UserService::sendEmailOtp for email - {}", email);

    if (userRepository.findByEmail(email).isPresent()) {
      throw new NftException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    String otp = OtpGenerator.generateSixDigitOtp();
    emailService.sendEmailOtp(email, otp);
    saveOtp(email, otp, EMAIL);
  }

  private void saveOtp(String key, String otp, String type) {
    Otp emailOtp = new Otp(key, otp, type);
    Optional<Otp> emailOtpOptional = otpRepository.findByTypeAndKey(type, key);
    if (emailOtpOptional.isPresent()) {
      emailOtp = emailOtpOptional.get();
      emailOtp.setOtp(otp);
    }
    otpRepository.save(emailOtp);
  }

  @Retryable(retryFor = UserCodeException.class)
  public void registerUser(UserRequest userRequest) {
    log.info("inside UserService::registerUser for email - {}", userRequest.email());

    User user = new User(userRequest);
    String userCode = AlphabeticalCodeGenerator.generateSixLetterCode();
    user.setUserCode(userCode);

    String email = user.getEmail();
    log.info("email - {} , generated userCode - {}", email, userCode);

    verifyOtp(email, userRequest.emailOtp(), EMAIL);
    verifyOtp(userRequest.phoneNo().toString(), userRequest.smsOtp(), MOBILE);
    verifyUserDetails(email, user);
    userRepository.save(user);
  }

  private void verifyUserDetails(String email, User user) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new NftException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new NftException(ErrorCode.USERNAME_ALREADY_EXISTS);
    }
    if (userRepository.existsByUserCode(user.getUserCode())) {
      throw new UserCodeException(ErrorCode.DUPLICATE_USER_CODE);
    }
  }

  public String loginUser(String email, String password) {
    log.info("inside UserService::loginUser for email - {}", email);

    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isPresent()) {

      String base64EncodedPassword = userOpt.get().getPassword();
      String savedPassword = new String(Base64.getDecoder().decode(base64EncodedPassword), StandardCharsets.UTF_8);

      if (savedPassword.equals(password)) {
        return jwtUtil.generateToken(userOpt.get().getEmail());
      }
    }
    throw new RuntimeException("Invalid credentials");
  }

  private void verifyOtp(String key, String otp, String type) {
    log.info("inside UserService::verifyOtp for key - {}", key);
    Optional<Otp> emailOtpOptional = otpRepository.findByTypeAndKey(type, key);
    if (emailOtpOptional.isPresent()) {
      Otp emailOtp = emailOtpOptional.get();
      if (emailOtp.getOtp().equals(otp)) {
        log.info("Otp verified for key - {}", key);
        return;
      }
    }
    throw new NftException(ErrorCode.INVALID_OTP);
  }

  public List<?> getUserReferralList(String email) {
    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isPresent()) {
      String userCode = userOptional.get().getUserCode();
      return userRepository.findByReferralCodeOrderByCreatedDateDesc(userCode).stream().map(UserDetailsResponse::new).toList();
    }
    throw new NftException(ErrorCode.USER_NOT_FOUND);
  }


  public void sendPasswordResetOtp(String email) {
    log.info("inside UserService::sendPasswordResetOtp for email - {}", email);

    validateUserEmail(email);

    String otp = OtpGenerator.generateSixDigitOtp();
    emailService.sendEmailOtp(email, otp);
    saveOtp(email, otp, EMAIL);
  }

  public void updatePassword(UserRequest userRequest) {
    String email = userRequest.email();
    log.info("inside UserService::updatePassword for email - {}", email);

    verifyOtp(email, userRequest.emailOtp(), EMAIL);

    User user = validateUserEmail(email);
    user.setPassword(userRequest.password());
    userRepository.save(user);

  }

  private User validateUserEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new NftException(ErrorCode.USER_NOT_FOUND));
  }

  public void sendMobileOtp(Integer mobileNo) {
    log.info("inside UserService::sendMobileOtp for mobileNo - {}", mobileNo);
    String otp = OtpGenerator.generateSixDigitOtp();
    saveOtp(mobileNo.toString(), otp, MOBILE);
    String smsResponse = smsService.sendSms(otp, mobileNo);
    log.info("mobileNo - {}, sendSms response - {}", mobileNo, smsResponse);
  }

}
