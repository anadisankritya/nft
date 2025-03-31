package com.nft.app.config;

import com.nft.app.entity.AppConfig;
import com.nft.app.entity.DepositRequest;
import com.nft.app.repository.AppConfigRepository;
import com.nft.app.repository.DepositRequestRepository;
import com.nft.app.repository.WalletMasterRepository;
import com.nft.app.repository.WithdrawRequestRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

  private final AppConfigRepository appConfigRepository;
  private final WithdrawRequestRepository withdrawRequestRepository;
  private final DepositRequestRepository depositRequestRepository;
  private final WalletMasterRepository walletMasterRepository;

  @PostConstruct
  public void init() {
    AppConfig appConfig = new AppConfig();
    appConfig.setOtpRequired(false);
    appConfig.setReferralCodeMandatory(false);
    appConfig.setMinWithdrawDays(7);
    appConfig.setBlockProfitSharing(Boolean.TRUE);
    appConfig.setMaxReferralPerDay(10);
    appConfigRepository.deleteAll();
    appConfigRepository.save(appConfig);

//    WithdrawRequest withdrawRequest = new WithdrawRequest("abc" + RandomUtils.secure().randomInt() + "@xyz.com", 100);
//    withdrawRequestRepository.save(withdrawRequest);
//
//    DepositRequest depositRequest = getDepositRequest();
//    depositRequestRepository.save(depositRequest);

//    WalletMaster walletMaster = new WalletMaster();
//    walletMaster.setWalletName("wallet1");
//    walletMaster.setBep20Address("bep20address");
//    walletMaster.setTrc20Address("trc20address");
//    walletMasterRepository.deleteAll();
//    walletMasterRepository.save(walletMaster);


  }

  private static DepositRequest getDepositRequest() {
    DepositRequest depositRequest = new DepositRequest();
    depositRequest.setEmail("abc" + RandomUtils.secure().randomInt() + "@xyz.com");
    depositRequest.setStatus("PENDING");
    depositRequest.setAmount(500);
    depositRequest.setTransactionId(String.valueOf(RandomUtils.secure().randomInt()));
    depositRequest.setWalletName("dummyWallet");
    return depositRequest;
  }


  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

}
