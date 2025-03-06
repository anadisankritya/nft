package com.nft.app.dto;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
  private String otp;
}