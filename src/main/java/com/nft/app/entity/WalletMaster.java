package com.nft.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "walletMaster")
public class WalletMaster {

  @Id
  private String id;

  @Indexed(unique = true)
  private String walletName;
  private String trc20Address;
  private String bep20Address;
}
