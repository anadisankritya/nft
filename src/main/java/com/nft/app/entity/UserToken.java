package com.nft.app.entity;

import com.nft.app.dto.request.LoginRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Document(collection = "userToken")
public class UserToken {

  @Id
  private String id;
  private String email;
  private String token;
  private String deviceId;
  private String imei;
  private boolean active;

  @Indexed(expireAfter = "0")
  private LocalDateTime expiryDate;

  public UserToken(LoginRequest loginRequest, String token) {
    this.email = loginRequest.email();
    this.token = token;
    this.deviceId = loginRequest.deviceId();
    this.imei = loginRequest.imei();
    this.active = true;
    this.expiryDate = LocalDateTime.now().plusDays(1);
  }

  public UserToken(UserToken userToken, String token) {
    this.email = userToken.getEmail();
    this.token = token;
    this.deviceId = userToken.getDeviceId();
    this.imei = userToken.getImei();
    this.active = true;
    this.expiryDate = LocalDateTime.now().plusDays(1);
  }

}
