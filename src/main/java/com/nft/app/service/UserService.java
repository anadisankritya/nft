package com.nft.app.service;

import com.nft.app.entity.User;
import com.nft.app.repository.UserRepository;
import com.nft.app.util.AlphabeticalCodeGenerator;
import com.nft.app.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final EmailService emailService;

  public void registerUser(User user) {
    log.info("inside UserService::registerUser for email - {}", user.getEmail());

    String userCode = AlphabeticalCodeGenerator.generateSixLetterCode();
    user.setUserCode(userCode);
    log.info("email - {} , generated userCode - {}", user.getEmail(), userCode);

    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new RuntimeException("Email already exists");
    }
    if (userRepository.findByUserCode(user.getUserCode()).isPresent()) {
      throw new RuntimeException("Something went wrong");
    }
    emailService.sendOtpEmail(user.getEmail());
    userRepository.save(user);
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
}
