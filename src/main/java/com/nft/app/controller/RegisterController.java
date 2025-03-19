package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.UserRequest;
import com.nft.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

  private final UserService userService;

  @PostMapping("/api/v1/send-email-otp")
  public ResponseEntity<NftResponse<String>> sendEmailOtp(@RequestParam String email) {
    userService.sendEmailOtp(email);
    return ResponseEntity.ok(new NftResponse<>("OTP sent"));
  }

  @PostMapping("/api/v1/send-phone-otp")
  public ResponseEntity<NftResponse<String>> sendPhoneOtp(@RequestParam String mobileNo) {
    userService.sendMobileOtp(mobileNo);
    return ResponseEntity.ok(new NftResponse<>("OTP sent"));
  }

  @PostMapping("/api/v1/signup")
  public ResponseEntity<NftResponse<String>> signup(@RequestBody @Valid UserRequest userRequest) {
    userService.registerUser(userRequest);
    return ResponseEntity.ok(new NftResponse<>("User registered"));
  }

  @PostMapping("/api/v1/reset-password")
  public ResponseEntity<NftResponse<String>> resetPassword(@RequestParam String email) {
    userService.sendPasswordResetOtp(email);
    return ResponseEntity.ok(new NftResponse<>("Password reset emailOtp sent"));
  }

  @PostMapping("/api/v1/update-password")
  public ResponseEntity<NftResponse<String>> updatePassword(@RequestBody UserRequest userRequest) {
    userService.updatePassword(userRequest);
    return ResponseEntity.ok(new NftResponse<>("Password updated"));

  }

}
