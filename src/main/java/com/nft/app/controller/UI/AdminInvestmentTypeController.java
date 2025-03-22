package com.nft.app.controller.UI;

import com.nft.app.service.InvestmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/ui/investment-types")
public class AdminInvestmentTypeController {

    @Autowired
    private InvestmentTypeService investmentTypeService;

    @GetMapping("/list")
    public String getAllInvestmentTypes(Model model) {
        return "InvestmentTypeCRUD";
    }
}