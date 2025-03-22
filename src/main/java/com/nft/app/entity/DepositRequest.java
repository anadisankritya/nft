package com.nft.app.entity;

import com.nft.app.dto.request.FundDepositRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "depositRequest")
public class DepositRequest {

  @Id
  private String id;
  private String email;
  private Integer amount;
  private String walletName;
  private String transactionId;
  private String status;
  private String comment;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime updatedDate;

  public DepositRequest(String email, String walletName, FundDepositRequest depositRequest) {
    this.email = email;
    this.amount = depositRequest.amount();
    this.walletName = walletName;
    this.transactionId = depositRequest.transactionId();
    this.status = "PENDING";
  }
}
