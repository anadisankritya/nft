package com.nft.app.dto;

import com.nft.app.entity.User;
import lombok.Data;

@Data
public class UserDetailsResponse {

  private String userName;
  private String email;

  public UserDetailsResponse(User user) {
    this.userName = user.getUsername();
//    this.email = user.getEmail();
  }
}
