package com.nft.app.entity;

import com.nft.app.dto.request.UserRequest;
import com.nft.app.util.Base64Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {

  @Id
  private String id;

  @Indexed(unique = true)
  private String username;

  @Indexed(unique = true)
  private String email;
  private String password;

  @Indexed(unique = true)
  private String userCode;
  private String referralCode;

  @Indexed(unique = true)
  private String phoneNo;
  private String levelId;
  private String walletId;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime updatedDate;

  public User(UserRequest userRequest) {
    this.username = userRequest.username();
    this.email = userRequest.email();
    this.password = Base64Utils.encodeString(userRequest.password());
    this.referralCode = userRequest.referralCode();
    this.phoneNo = userRequest.phoneNo();
  }
}
