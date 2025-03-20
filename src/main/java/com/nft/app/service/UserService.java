package com.nft.app.service;

import com.nft.app.constant.AppConstants;
import com.nft.app.dto.UserDetailsResponse;
import com.nft.app.dto.UserRequest;
import com.nft.app.entity.AppConfig;
import com.nft.app.entity.User;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import com.nft.app.exception.UserCodeException;
import com.nft.app.repository.AppConfigRepository;
import com.nft.app.repository.UserRepository;
import com.nft.app.util.AlphabeticalCodeGenerator;
import com.nft.app.util.Base64Utils;
import com.nft.app.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private AppConfig appConfig;

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final AppConfigRepository appConfigRepository;
  private final OtpService otpService;

  @PostConstruct
  private void init() {
    List<AppConfig> appConfigList = appConfigRepository.findAll();
    if (!appConfigList.isEmpty()) {
      appConfig = appConfigList.getFirst();
    }
  }

  public void sendEmailOtp(String email) {
    log.info("inside UserService::sendEmailOtp for email - {}", email);

    if (userRepository.findByEmail(email).isPresent()) {
      throw new NftException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    if (otpService.checkOtpRecentlySent(email, AppConstants.EMAIL)) {
      log.info("Otp already sent");
      return;
    }
    otpService.sendOtp(email, AppConstants.EMAIL);
  }

  public void sendMobileOtp(String mobileNo) {
    log.info("inside UserService::sendMobileOtp for mobileNo - {}", mobileNo);

    if (userRepository.existsByPhoneNo(mobileNo)) {
      throw new NftException(ErrorCode.MOBILE_NO_ALREADY_EXISTS);
    }

    if (otpService.checkOtpRecentlySent(mobileNo, AppConstants.MOBILE)) {
      log.info("Otp already sent");
      return;
    }

    otpService.sendOtp(mobileNo, AppConstants.MOBILE);

  }

  @Retryable(retryFor = UserCodeException.class)
  public void registerUser(UserRequest userRequest) {
    log.info("inside UserService::registerUser for email - {}", userRequest.email());

    User user = new User(userRequest);
    String userCode = AlphabeticalCodeGenerator.generateEightLetterCode();
    user.setUserCode(userCode);

    String email = user.getEmail();
    log.info("email - {} , generated userCode - {}", email, userCode);

    if (BooleanUtils.isTrue(appConfig.getOtpRequired())) {
      otpService.verifyOtp(email, userRequest.emailOtp(), AppConstants.EMAIL);
      otpService.verifyOtp(userRequest.phoneNo(), userRequest.smsOtp(), AppConstants.MOBILE);
    }
    verifyUserDetails(user);
    userRepository.save(user);
  }

  private void verifyUserDetails(User user) {
    log.info("inside UserService::verifyUserDetails for email - {}", user.getEmail());

    boolean existsByUserCode = userRepository.existsByUserCode(user.getUserCode());

    //validate referral code
    if (appConfig != null && appConfig.getReferralCodeMandatory()) {
      if (!StringUtils.hasText(user.getReferralCode())) {
        throw new NftException(ErrorCode.REFERRAL_CODE_MANDATORY);
      }
      if (!existsByUserCode) {
        throw new NftException(ErrorCode.INVALID_REFERRAL_CODE);
      }
    }

    //validate user email
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new NftException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    //validate username
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new NftException(ErrorCode.USERNAME_ALREADY_EXISTS);
    }

    //validate user code
    if (existsByUserCode) {
      throw new UserCodeException(ErrorCode.DUPLICATE_USER_CODE);
    }
  }

  public String loginUser(String email, String password) {
    log.info("inside UserService::loginUser for email - {}", email);

    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isPresent()) {

      String base64EncodedPassword = userOpt.get().getPassword();
      String savedPassword = Base64Utils.decodeToString(base64EncodedPassword);

      if (savedPassword.equals(password)) {
        return jwtUtil.generateToken(userOpt.get().getEmail());
      }
    }
    throw new RuntimeException("Invalid credentials");
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

    getUser(email);

    if (otpService.checkOtpRecentlySent(email, AppConstants.EMAIL)) {
      log.info("Otp already sent");
      return;
    }
    otpService.sendOtp(email, AppConstants.EMAIL);
  }

  public void updatePassword(UserRequest userRequest) {
    String email = userRequest.email();
    log.info("inside UserService::updatePassword for email - {}", email);

    otpService.verifyOtp(email, userRequest.emailOtp(), AppConstants.EMAIL);

    User user = getUser(email);
    user.setPassword(Base64Utils.encodeString(userRequest.password()));
    userRepository.save(user);
  }

  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new NftException(ErrorCode.USER_NOT_FOUND));
  }

}
