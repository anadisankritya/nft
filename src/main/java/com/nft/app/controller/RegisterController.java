package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.UserRequest;
import com.nft.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<NftResponse<Void>> sendEmailOtp(@RequestParam String email) {
    userService.sendEmailOtp(email);
    return ResponseEntity.ok(new NftResponse<>("OTP sent", null));
  }

  @PostMapping("/api/v1/send-phone-otp")
  public ResponseEntity<NftResponse<Void>> sendPhoneOtp(@RequestParam String mobileNo) {
    userService.sendMobileOtp(mobileNo);
    return ResponseEntity.ok(new NftResponse<>("OTP sent", null));
  }

  @PostMapping("/api/v1/signup")
  public ResponseEntity<NftResponse<Void>> signup(@RequestBody @Valid UserRequest userRequest) {
    userService.registerUser(userRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new NftResponse<>("User registered", null));
  }

  @PostMapping("/api/v1/reset-password")
  public ResponseEntity<NftResponse<Void>> resetPassword(@RequestParam String email) {
    userService.sendPasswordResetOtp(email);
    return ResponseEntity.ok(new NftResponse<>("Password reset otp sent", null));
  }

  @PostMapping("/api/v1/update-password")
  public ResponseEntity<NftResponse<Void>> updatePassword(@RequestBody UserRequest userRequest) {
    userService.updatePassword(userRequest);
    return ResponseEntity.ok(new NftResponse<>("Password updated", null));

  }

}
