package com.nft.app.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "appConfig")
public class AppConfig {

  @Id
  private String id;
  private Boolean referralCodeMandatory;
}
