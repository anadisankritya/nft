package com.nft.app.controller.UI;

import com.nft.app.dto.request.CreateInvestmentRequest;
import com.nft.app.service.InvestmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/ui/")
public class UIInvestmentTypeController {

  @Autowired
  private InvestmentTypeService investmentTypeService;

  @GetMapping("/")
  public String indexForm(Model model) {
    return "UI";
  }

  @GetMapping("/investment-types/create")
  public String showCreateForm(Model model) {
    model.addAttribute("investmentType", new CreateInvestmentRequest());
    return "create";
  }

  @GetMapping("/investment-types/list")
  public String getAllInvestmentTypes(Model model) {
    model.addAttribute("investmentType", new CreateInvestmentRequest());
    return "list";
  }

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