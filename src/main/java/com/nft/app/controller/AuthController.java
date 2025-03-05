package com.nft.app.controller;

import com.nft.app.dto.LoginRequest;
import com.nft.app.dto.UserRequest;
import com.nft.app.entity.User;
import com.nft.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody UserRequest userRequest) {
    try {
      User user = new User(userRequest);
      userService.registerUser(user);
      return ResponseEntity.ok("user registered");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    try {
      String token = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
      return ResponseEntity.ok(token);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
