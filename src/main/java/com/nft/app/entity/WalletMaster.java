package com.nft.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "walletMaster")
public class WalletMaster {

  @Id
  private String id;

  private String walletName;
  private String trc20Address;
  private String bep20Address;
}
