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
  private Integer level;
  private WalletDetails walletDetails;
  private Integer walletBalance;

  public UserDetails(User user) {
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.userCode = user.getUserCode();
    this.phoneNo = user.getPhoneNo();
    this.level = user.getLevel();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class WalletDetails{
    private String trc20Address;
    private String bep20Address;
    private Integer walletBalance;
  }
}
