package com.nft.app.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/ui/wallet")
public class WalletRequestController {

  @GetMapping("/pending-withdrawal")
  public String showWithdrawRequestsPage() {
    return "pending_withdrawal";
  }

  @GetMapping("/pending-deposit")
  public String showDepositRequestsPage() {
    return "pending_deposit";
  }

  @GetMapping("/withdrawal-history")
  public String showWithdrawHistoryPage() {
    return "withdrawal_history";
  }

  @GetMapping("/deposit-history")
  public String showDepositHistoryPage() {
    return "deposit_history";
  }

}