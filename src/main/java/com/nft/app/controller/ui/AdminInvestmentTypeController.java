package com.nft.app.controller.ui;

import com.nft.app.constant.AppConstants;
import com.nft.app.service.InvestmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/investment-types")
public class AdminInvestmentTypeController {

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute(
                "columns", AppConstants.INVESTMENT_TYPE_TABLE_COLUMN
        );
        model.addAttribute("pageTitle", "Investment Types");
        return "page/investment_types";
    }
}