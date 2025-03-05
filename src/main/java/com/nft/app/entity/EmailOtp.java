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
@Document(collection = "emailOtp")
public class EmailOtp {
  @Id
  private String id;
  private String email;
  private String otp;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime updatedDate;


  public EmailOtp(String email, String otp){
    this.email = email;
    this.otp = otp;
  }
}
