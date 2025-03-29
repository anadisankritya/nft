package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.LoginRequest;
import com.nft.app.dto.response.UserDetails;
import com.nft.app.dto.response.UserTeamResponse;
import com.nft.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/api/v1/login")
  public ResponseEntity<NftResponse<?>> login(@RequestBody LoginRequest loginRequest) {
    String token = userService.loginUser(loginRequest.email(), loginRequest.password());
    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("accessToken", token);
    return ResponseEntity.ok(new NftResponse<>("Login success", responseMap));
  }

  @GetMapping("/api/v1/my-profile")
  public ResponseEntity<NftResponse<UserDetails>> userDetails() {
    String email = getUserEmail();
    UserDetails userDetails = userService.getUserDetails(email);
    return ResponseEntity.ok(new NftResponse<>("My team list fetched", userDetails));
  }

  @GetMapping("/api/v1/my-team")
  public ResponseEntity<NftResponse<UserTeamResponse>> referList() {
    String email = getUserEmail();
    UserTeamResponse userTeamList = userService.getUserTeamList(email);
    return ResponseEntity.ok(new NftResponse<>("My team list fetched", userTeamList));
  }

  private static String getUserEmail() {
    String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    return email;
  }

}
