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

  @PostMapping("/send-otp")
  public ResponseEntity<NftResponse<String>> sendOtp(@RequestParam String email) {
    userService.sendEmailOtp(email);
    return ResponseEntity.ok(new NftResponse<>("Otp sent"));
  }

  @PostMapping("/signup")
  public ResponseEntity<NftResponse<String>> signup(@RequestBody UserRequest userRequest) {
    try {
      userService.registerUser(userRequest);
      return ResponseEntity.ok(new NftResponse<>("user registered"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new NftResponse<>(e.getMessage()));
    }
  }

}
