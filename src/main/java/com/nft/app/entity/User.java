package com.nft.app.entity;

import com.nft.app.dto.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
  @Id
  private String id;
  private String username;
  private String email;
  private String password;
  private String userCode;
  private String referralCode;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime updatedDate;

  public User(UserRequest userRequest) {
    this.username = userRequest.username();
    this.email = userRequest.email();
    this.password = Base64.getEncoder().
        encodeToString(userRequest.password().getBytes(StandardCharsets.UTF_8));
    this.referralCode = userRequest.referralCode();
  }
}
