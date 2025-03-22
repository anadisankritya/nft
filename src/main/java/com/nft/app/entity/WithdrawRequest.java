package com.nft.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "withdrawRequest")
public class WithdrawRequest {

  @Id
  private String id;
  private String email;
  private Integer amount;
  private Integer serviceCharge;
  private Integer totalAmount;
  private String status;
  private String comment;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime updatedDate;

  public WithdrawRequest(String email, Integer amount) {
    this.email = email;
    this.amount = amount;
    this.serviceCharge = (int) Math.ceil(0.05 * amount);
    this.totalAmount = amount + serviceCharge;
    this.status = "PENDING";
  }
}
