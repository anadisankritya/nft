package com.nft.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "transactionRecord")
public class TransactionRecord {

  @Id
  private String id;

  private String email;
  private Double previousBalance;
  private Double changeAmount;
  private Double newBalance;
  private String type;

  private Integer txnNo;

  @CreatedDate
  private LocalDateTime createdDate;

  public TransactionRecord(String email, Double currentAmount, Double changeAmount, String type) {
    this.email = email;
    this.previousBalance = currentAmount;
    this.changeAmount = changeAmount;
    this.newBalance = currentAmount - changeAmount;
    this.type = type;
  }
}
