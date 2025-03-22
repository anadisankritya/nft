package com.nft.app.controller.UI;

import com.nft.app.dto.InvestmentTypeDto;
import com.nft.app.mapper.InvestmentTypeMapper;
import com.nft.app.service.InvestmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ui/")
public class UIController {

    @Autowired
    private InvestmentTypeService investmentTypeService;

    @GetMapping("/investment-types")
    public String getInvestmentTypesUI(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        model.addAttribute("investmentTypes", investmentTypeService.getAllInvestmentTypes(page, size)
                .stream()
                .map(InvestmentTypeMapper::toDto)
                .toList());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "list";
    }

    @GetMapping("/investment-types/create")
    public String showCreateForm(Model model) {
        model.addAttribute("investmentType", new InvestmentTypeDto());
        return "create";
    }

    @PostMapping("/investment-types/create")
    public String createInvestmentType(@ModelAttribute InvestmentTypeDto investmentTypeDto,
                                       BindingResult bindingResult, Model model) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "create"; // Return to the form page with validation errors
        }

        // Create the investment type
        investmentTypeService.createInvestmentType(InvestmentTypeMapper.toEntity(investmentTypeDto));
        return "redirect:/ui/investment-types";
    }

    @GetMapping("/investment-types/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        InvestmentTypeDto investmentTypeDto = InvestmentTypeMapper.toDto(
                investmentTypeService.getInvestmentTypeById(id)
                        .orElseThrow(() -> new RuntimeException("InvestmentType not found"))
        );
        model.addAttribute("investmentType", investmentTypeDto);
        return "edit";
    }

    private Map<String, String> parseMetadata(String metadataString) {
        Map<String, String> metadataMap = new HashMap<>();
        if (metadataString == null || metadataString.trim().isEmpty()) {
            return metadataMap; // Return an empty map if metadata is null or empty
        }

        // Remove curly braces if present (e.g., "{color=bond, priority=equity}")
        metadataString = metadataString.replaceAll("[{}]", "");

        // Split the string by commas to get individual key-value pairs
        String[] entries = metadataString.split(",");
        for (String entry : entries) {
            // Split each entry by "=" to separate key and value
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                metadataMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return metadataMap;
    }

    @PostMapping("/investment-types/edit/{id}")
    public String updateInvestmentType(@PathVariable String id, @ModelAttribute InvestmentTypeDto updatedInvestmentTypeDto) {
        investmentTypeService.updateInvestmentType(id, InvestmentTypeMapper.toEntity(updatedInvestmentTypeDto));
        return "redirect:/ui/investment-types";
    }

    @GetMapping("/investment-types/delete/{id}")
    public String deleteInvestmentType(@PathVariable String id) {
        investmentTypeService.deleteInvestmentType(id);
        return "redirect:/ui/investment-types";
    }
}