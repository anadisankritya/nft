package com.nft.app.controller;

import com.nft.app.dto.InvestmentTypeDto;
import com.nft.app.mapper.InvestmentTypeMapper;
import com.nft.app.service.InvestmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/investment-types")
public class InvestmentTypeController {

    @Autowired
    private InvestmentTypeService investmentTypeService;

    @GetMapping
    public List<InvestmentTypeDto> getInvestmentTypes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return investmentTypeService.getAllInvestmentTypes(page, size)
                .stream()
                .map(InvestmentTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public InvestmentTypeDto createInvestmentType(@RequestBody InvestmentTypeDto investmentTypeDto) {
        return InvestmentTypeMapper.toDto(
                investmentTypeService.createInvestmentType(
                        InvestmentTypeMapper.toEntity(investmentTypeDto)
                )
        );
    }

    @PutMapping("/{id}")
    public InvestmentTypeDto updateInvestmentType(@PathVariable String id, @RequestBody InvestmentTypeDto updatedInvestmentTypeDto) {
        return InvestmentTypeMapper.toDto(
                investmentTypeService.updateInvestmentType(
                        id,
                        InvestmentTypeMapper.toEntity(updatedInvestmentTypeDto)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestmentType(@PathVariable String id) {
        investmentTypeService.deleteInvestmentType(id);
        return ResponseEntity.noContent().build();
    }
}