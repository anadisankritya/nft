package com.nft.app.dto.response;

import com.nft.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDetails {
  private String username;
  private String email;
  private String userCode;
  private String phoneNo;
  private String levelName;
  private String levelId;
  private WalletDetails walletDetails;
  private Double walletBalance;

  public UserDetails(User user) {
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.userCode = user.getUserCode();
    this.phoneNo = user.getPhoneNo();
    this.levelId = user.getLevelId();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WalletDetails {
    private String trc20Address;
    private String bep20Address;
    private Double walletBalance;
  }
}
