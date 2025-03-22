package com.nft.app.controller.UI;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/ui/")
public class AdminDashboardController {
    @GetMapping("/")
    public String indexForm(Model model) {
        return "Dashboard";
    }
}