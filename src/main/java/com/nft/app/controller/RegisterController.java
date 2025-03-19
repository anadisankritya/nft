package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.UserRequest;
import com.nft.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register/api/v1")
@RequiredArgsConstructor
public class RegisterController {

  private final UserService userService;

  @PostMapping("/send-email-otp")
  public ResponseEntity<NftResponse<String>> sendEmailOtp(@RequestParam String email) {
    userService.sendEmailOtp(email);
    return ResponseEntity.ok(new NftResponse<>("OTP sent"));
  }

  @PostMapping("/send-phone-otp")
  public ResponseEntity<NftResponse<String>> sendPhoneOtp(@RequestParam String mobileNo) {
    userService.sendMobileOtp(mobileNo);
    return ResponseEntity.ok(new NftResponse<>("OTP sent"));
  }

  @PostMapping("/signup")
  public ResponseEntity<NftResponse<String>> signup(@RequestBody UserRequest userRequest) {
    try {
      userService.registerUser(userRequest);
      return ResponseEntity.ok(new NftResponse<>("User registered"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new NftResponse<>(e.getMessage()));
    }
  }

  @PostMapping("/reset-password")
  public ResponseEntity<NftResponse<String>> resetPassword(@RequestParam String email) {
    try {
      userService.sendPasswordResetOtp(email);
      return ResponseEntity.ok(new NftResponse<>("Password reset emailOtp sent"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new NftResponse<>(e.getMessage()));
    }
  }

  @PostMapping("/update-password")
  public ResponseEntity<NftResponse<String>> updatePassword(@RequestBody UserRequest userRequest) {
    try {
      userService.updatePassword(userRequest);
      return ResponseEntity.ok(new NftResponse<>("Password updated"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new NftResponse<>(e.getMessage()));
    }
  }

}
