package com.nft.app.controller.ui;

import com.nft.app.constant.AppConstants;
import com.nft.app.service.InvestmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/nft-upload")
public class AdminNFTUploadController {

    @GetMapping("/")
    public String indexForm(Model model) {
        model.addAttribute(
                "columns", AppConstants.NFT_UPLOAD_TABLE_COLUMN
        );
        model.addAttribute("pageTitle", "NFT UPLOAD");
        return "page/nft_upload";
    }
}