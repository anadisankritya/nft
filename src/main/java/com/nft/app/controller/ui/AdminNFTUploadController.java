package com.nft.app.controller.ui;

import com.nft.app.service.InvestmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/ui/nft")
public class AdminNFTUploadController {

    @Autowired
    private InvestmentTypeService investmentTypeService;

    @GetMapping("/list")
    public String getAllInvestmentTypes(Model model) {
        return "/nft/index";
    }
}