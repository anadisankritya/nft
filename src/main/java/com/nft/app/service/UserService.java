package com.nft.app.service;

import com.nft.app.dto.UserDetailsResponse;
import com.nft.app.dto.UserRequest;
import com.nft.app.entity.EmailOtp;
import com.nft.app.entity.User;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import com.nft.app.exception.UserCodeException;
import com.nft.app.repository.EmailOtpRepository;
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

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final EmailService emailService;
  private final EmailOtpRepository emailOtpRepository;

  public void sendEmailOtp(String email) {
    log.info("inside UserService::sendEmailOtp for email - {}", email);

    if (userRepository.findByEmail(email).isPresent()) {
      throw new NftException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    String otp = OtpGenerator.generateSixDigitOtp();
    emailService.sendEmailOtp(email, otp);
    saveEmailOtp(email, otp);
  }

  private void saveEmailOtp(String email, String otp) {
    EmailOtp emailOtp = new EmailOtp(email, otp);
    Optional<EmailOtp> emailOtpOptional = emailOtpRepository.findByEmail(email);
    if (emailOtpOptional.isPresent()) {
      emailOtp = emailOtpOptional.get();
      emailOtp.setOtp(otp);
    }
    emailOtpRepository.save(emailOtp);
  }

  @Retryable(retryFor = UserCodeException.class)
  public void registerUser(UserRequest userRequest) {
    log.info("inside UserService::registerUser for email - {}", userRequest.email());

    User user = new User(userRequest);
    String userCode = AlphabeticalCodeGenerator.generateSixLetterCode();
    user.setUserCode(userCode);

    String email = user.getEmail();
    log.info("email - {} , generated userCode - {}", email, userCode);

    verifyOtp(email, userRequest.otp());
    verifyUserDetails(email, user);
    userRepository.save(user);
  }

  private void verifyUserDetails(String email, User user) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new NftException(ErrorCode.EMAIL_ALREADY_EXISTS);
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

  private void verifyOtp(String email, String otp) {
    log.info("inside UserService::verifyOtp for email - {}", email);
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

  public List<?> getUserReferralList(String userCode) {
    return userRepository.findByReferralCodeOrderByCreatedDateDesc(userCode).stream().map(UserDetailsResponse::new).toList();
  }

}
