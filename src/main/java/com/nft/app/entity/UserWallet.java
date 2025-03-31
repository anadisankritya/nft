package com.nft.app.entity;

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
@Document(collection = "userWallet")
public class UserWallet {

  @Id
  private String id;

  @Indexed(unique = true)
  private String email;
  private double balance;

  private double maxBalance;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime updatedDate;

  public void setBalance(final double balance) {
    this.balance = balance;
    this.maxBalance = Math.max(this.balance, this.maxBalance);
  }

}
