package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.CreateInvestmentRequest;
import com.nft.app.dto.response.CreateInvestmentResponse;
import com.nft.app.service.InvestmentTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/investment-types")
public class InvestmentTypeController {

    @Autowired
    private InvestmentTypeService investmentTypeService;

    @GetMapping("/list")
    public ResponseEntity<NftResponse<List<CreateInvestmentResponse>>> getInvestmentTypes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("Investment data",
                        investmentTypeService.getAllInvestmentTypes(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NftResponse<CreateInvestmentResponse>> getInvestmentTypes(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("Investment data",
                        investmentTypeService.getInvestmentTypeById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<NftResponse<String>> createInvestment(
            @Valid @RequestBody CreateInvestmentRequest createInvestmentRequest
    ) {
        investmentTypeService.createInvestmentType(
                createInvestmentRequest
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new NftResponse<>("201","Investment Created Successfully",null
                        ));
    }

    @PutMapping("/update")
    public ResponseEntity<NftResponse<String>> updateInvestment(@Valid @RequestBody CreateInvestmentRequest createInvestmentRequest) {
        investmentTypeService.updateInvestmentType(
                createInvestmentRequest
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("Investment Updated Successfully",null
                ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<NftResponse<Object>> deleteInvestmentType(@PathVariable String id) {
        investmentTypeService.deleteInvestmentType(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new NftResponse<>("Investment Deleted Successfully",null
                ));
    }
}