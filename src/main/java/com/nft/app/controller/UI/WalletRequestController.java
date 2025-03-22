package com.nft.app.controller.UI;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/ui/wallet")
public class WalletRequestController {

  @GetMapping("/pending-withdrawal")
  public String showWithdrawRequestsPage() {
    // The returned string corresponds to the name of the HTML file in templates (without the extension).
    return "pending_withdrawal";
  }

  @GetMapping("/pending-deposit")
  public String showDepositRequestsPage() {
    // The returned string corresponds to the name of the HTML file in templates (without the extension).
    return "pending_deposit";
  }


}