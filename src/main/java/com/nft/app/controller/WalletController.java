package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.FundDepositRequest;
import com.nft.app.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

  private final WalletService walletService;

  @PostMapping("/api/v1/deposit")
  public NftResponse<Void> deposit(@RequestBody FundDepositRequest fundDepositRequest) {
    String email = getUserEmail();
    walletService.depositFund(email, fundDepositRequest);
    return new NftResponse<>("Deposit request sent", null);
  }

  @PostMapping("/api/v1/withdraw")
  public NftResponse<Void> deposit(@RequestParam(defaultValue = "0") Integer amount) {
    String email = getUserEmail();
    walletService.withdrawFund(email, amount);
    return new NftResponse<>("Withdraw request sent, " +
        "your money will be credited in 72 hours", null);
  }

  private static String getUserEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
  }

}
