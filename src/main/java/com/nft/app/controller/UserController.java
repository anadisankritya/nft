package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.LoginRequest;
import com.nft.app.dto.response.UserDetails;
import com.nft.app.dto.response.UserTeamResponse;
import com.nft.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    String token = userService.loginUser(loginRequest);
    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("accessToken", token);
    return ResponseEntity.ok(new NftResponse<>("Login success", responseMap));
  }

  @PostMapping("/api/v1/logout")
  public ResponseEntity<NftResponse<Void>> logout() {
    String email = getUserEmail();
    userService.logoutUser(email);
    return ResponseEntity.ok(new NftResponse<>("Logout success", null));
  }

  @GetMapping("/api/v1/regenerate-token")
  public ResponseEntity<NftResponse<?>> regenerateToken(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
    String regenerateToken = userService.regenerateToken(token);
    if (StringUtils.hasText(regenerateToken)) {
      Map<String, String> responseMap = new HashMap<>();
      responseMap.put("accessToken", token);
      return ResponseEntity.ok(new NftResponse<>("Token Regenerated", responseMap));
    }
    return ResponseEntity.badRequest().body(new NftResponse<>("Please Login again", null));
  }

  @GetMapping("/api/v1/my-profile")
  public ResponseEntity<NftResponse<UserDetails>> userDetails() {
    String email = getUserEmail();
    UserDetails userDetails = userService.getUserDetails(email);
    return ResponseEntity.ok(new NftResponse<>("User details fetched", userDetails));
  }

  @GetMapping("/api/v1/my-team")
  public ResponseEntity<NftResponse<UserTeamResponse>> referList() {
    String email = getUserEmail();
    UserTeamResponse userTeamList = userService.getUserTeamList(email);
    return ResponseEntity.ok(new NftResponse<>("My team list fetched", userTeamList));
  }

  @GetMapping("/api/v1/refer-app")
  public ResponseEntity<NftResponse<String>> referApp() {
    String email = getUserEmail();
    UserDetails userDetails = userService.getUserDetails(email);
    return ResponseEntity.ok(new NftResponse<>(
        "Download the app using link - https//app-link.com. Use referral code - " + userDetails.getUserCode(), null)
    );
  }

  private static String getUserEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
  }

}
