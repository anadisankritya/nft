package com.nft.app.service;

import com.nft.app.constant.AppConstants;
import com.nft.app.dto.request.LoginRequest;
import com.nft.app.dto.request.UserRequest;
import com.nft.app.dto.response.UserDetails;
import com.nft.app.dto.response.UserTeamResponse;
import com.nft.app.entity.AppConfig;
import com.nft.app.entity.User;
import com.nft.app.entity.UserLevel;
import com.nft.app.entity.UserToken;
import com.nft.app.entity.UserWallet;
import com.nft.app.entity.WalletMaster;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import com.nft.app.exception.UserCodeException;
import com.nft.app.repository.AppConfigRepository;
import com.nft.app.repository.UserRepository;
import com.nft.app.repository.UserTokenRepository;
import com.nft.app.repository.UserWalletRepository;
import com.nft.app.repository.WalletMasterRepository;
import com.nft.app.util.AlphabeticalCodeGenerator;
import com.nft.app.util.Base64Utils;
import com.nft.app.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  public static AppConfig appConfig;

  private final UserRepository userRepository;
  private final AppConfigRepository appConfigRepository;
  private final OtpService otpService;
  private final WalletMasterRepository walletMasterRepository;
  private final UserWalletRepository userWalletRepository;
  private final UserLevelService userLevelService;
  private final UserTokenRepository userTokenRepository;

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

  //  @Transactional
  @Retryable(retryFor = UserCodeException.class)
  public void registerUser(UserRequest userRequest) {
    log.info("inside UserService::registerUser for email - {}", userRequest.email());

    User user = new User(userRequest);
    String userCode = AlphabeticalCodeGenerator.generateEightLetterCode();
    user.setUserCode(userCode);
    user.setWalletId(getRandomWallet());

    UserLevel userLevel = userLevelService.getBaseUserLevel();
    user.setLevelId(userLevel.getId());

    String email = user.getEmail();
    log.info("email - {} , generated userCode - {}", email, userCode);

    if (BooleanUtils.isTrue(appConfig.getOtpRequired())) {
      otpService.verifyOtp(email, userRequest.emailOtp(), AppConstants.EMAIL);
      otpService.verifyOtp(userRequest.phoneNo(), userRequest.smsOtp(), AppConstants.MOBILE);
    }
    verifyUserDetails(user);
    createNewUserWallet(email);
    userRepository.save(user);
  }

  private void createNewUserWallet(String email) {
    UserWallet userWallet = new UserWallet();
    userWallet.setEmail(email);
    userWallet.setBalance(0);
    userWalletRepository.save(userWallet);
  }

  private void verifyUserDetails(User user) {
    log.info("inside UserService::verifyUserDetails for email - {}", user.getEmail());

    boolean existsByUserCode = userRepository.existsByUserCode(user.getUserCode());

    //validate referral code
    if (appConfig != null && appConfig.getReferralCodeMandatory()) {
      if (!StringUtils.hasText(user.getReferralCode())) {
        throw new NftException(ErrorCode.REFERRAL_CODE_MANDATORY);
      }
      boolean existsByReferralCode = userRepository.existsByUserCode(user.getReferralCode());
      if (!existsByReferralCode) {
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

    List<User> referralCodeUsedToday = userRepository.findByReferralCodeAndCreatedDateGreaterThanEqual(user.getReferralCode(), LocalDateTime.now().minusDays(1L));
    if (referralCodeUsedToday.size() >= appConfig.getMaxReferralPerDay()) {
      throw new NftException(ErrorCode.MAX_REFERRAL_EXCEEDED);
    }

    //validate user code
    if (existsByUserCode) {
      throw new UserCodeException(ErrorCode.DUPLICATE_USER_CODE);
    }
  }

  public String loginUser(LoginRequest loginRequest) {
    log.info("inside UserService::loginUser for email - {}", loginRequest.email());

    User user = getUser(loginRequest.email());
    String base64EncodedPassword = user.getPassword();
    String savedPassword = Base64Utils.decodeToString(base64EncodedPassword);

    if (savedPassword.equals(loginRequest.password())) {
      String token = JwtUtil.generateToken(user.getEmail());
      saveToken(loginRequest, token);
      return token;
    }
    throw new NftException(ErrorCode.INVALID_PASSWORD);
  }

  private void saveToken(LoginRequest loginRequest, String token) {
    UserToken userToken = new UserToken(loginRequest, token);
    expireOldTokens(loginRequest.email());
    userTokenRepository.save(userToken);
  }

  private void expireOldTokens(String email) {
    List<UserToken> userTokenList = userTokenRepository.findByEmailAndActive(email, true);
    if (!userTokenList.isEmpty()) {
      userTokenList.forEach(ut -> ut.setActive(false));
      userTokenRepository.saveAll(userTokenList);
    }
  }

  public UserTeamResponse getUserTeamList(String email) {
    log.info("inside UserService::getUserTeamList for email - {}", email);

    User user = getUser(email);
    String userCode = user.getUserCode();
    List<String> teamMembers = userRepository.findByReferralCodeOrderByCreatedDateDesc(userCode).stream().map(User::getUsername).toList();
    return new UserTeamResponse(teamMembers);

  }

  public UserDetails getUserDetails(String email) {
    log.info("inside UserService::getUserDetails for email - {}", email);
    User user = getUser(email);

    UserWallet userWallet = userWalletRepository.findByEmail(user.getEmail());
    Optional<WalletMaster> walletMasterOptional = walletMasterRepository.findById(user.getWalletId());
    if (walletMasterOptional.isEmpty()) {
      throw new NftException(ErrorCode.WALLET_NOT_FOUND);
    }

    WalletMaster walletMaster = walletMasterOptional.get();
    UserDetails.WalletDetails walletDetails =
        new UserDetails.WalletDetails(walletMaster.getTrc20Address(), walletMaster.getBep20Address(), userWallet.getBalance());

    UserDetails userDetails = new UserDetails(user);
    String levelName = userLevelService.getUserLevelById(user.getLevelId()).getName();
    userDetails.setLevelName(levelName);
    userDetails.setWalletDetails(walletDetails);
    userDetails.setWalletBalance(userWallet.getBalance());
    return userDetails;
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

  User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new NftException(ErrorCode.USER_NOT_FOUND));
  }

  private String getRandomWallet() {
    List<WalletMaster> walletMasterList = walletMasterRepository.findAll();
    int randomWalletNo = RandomUtils.secure().randomInt(0, walletMasterList.size());
    return walletMasterList.get(randomWalletNo).getId();
  }

  public void logoutUser(String email) {
    expireOldTokens(email);
  }

}
