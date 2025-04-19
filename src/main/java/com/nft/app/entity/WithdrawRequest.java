package com.nft.app.entity;

import com.nft.app.dto.request.WithdrawRequestDto;
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
  private Double serviceCharge;
  private Double totalAmount;
  private String status;
  private String network;
  private String walletAddress;
  private String comment;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime updatedDate;

  public WithdrawRequest(String email, WithdrawRequestDto request) {
    this.email = email;
    this.amount = request.amount();
    this.serviceCharge = Math.ceil(0.05 * amount);
    this.totalAmount = this.amount + this.serviceCharge;
    this.network = request.network();
    this.walletAddress = request.walletAddress();
    this.status = "PENDING";
  }
}
