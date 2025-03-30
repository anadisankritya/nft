package com.nft.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "transactionRecord")
public class TransactionRecord {

  @Id
  private String id;

  private String email;
  private Double previousAmount;
  private Double changeAmount;
  private Double newAmount;
  private String type;
}
