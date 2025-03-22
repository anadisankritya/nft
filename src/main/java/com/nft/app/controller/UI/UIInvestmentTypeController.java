package com.nft.app.controller.UI;

import com.nft.app.dto.request.CreateInvestmentRequest;
import com.nft.app.service.InvestmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
}