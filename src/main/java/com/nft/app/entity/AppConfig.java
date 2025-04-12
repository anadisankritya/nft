package com.nft.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "appConfig")
public class AppConfig {

  @Id
  private String id;
  private Boolean referralCodeMandatory;
  private Boolean otpRequired;
  private Integer minWithdrawDays;
  private Boolean blockProfitSharing;
  private Integer maxReferralPerDay;

}
