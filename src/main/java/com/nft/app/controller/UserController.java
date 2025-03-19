package com.nft.app.controller;

import com.nft.app.dto.LoginRequest;
import com.nft.app.dto.NftResponse;
import com.nft.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/api/v1")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<NftResponse<?>> login(@RequestBody LoginRequest loginRequest) {
    String token = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("accessToken", token);
    return ResponseEntity.ok(new NftResponse<>("Login success", responseMap));
  }

  @PostMapping("/refer-list")
  public ResponseEntity<NftResponse<?>> referList() {
    String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    List<?> userReferralList = userService.getUserReferralList(email);
    return ResponseEntity.ok(new NftResponse<>("My team list fetched", userReferralList));
  }

}
