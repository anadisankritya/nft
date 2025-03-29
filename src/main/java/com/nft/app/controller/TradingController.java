package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.BuyOrderDto;
import com.nft.app.service.TradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trade/")
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService;

    @PostMapping("/api/v1/order/create")
    public ResponseEntity<NftResponse<String>> createBuyOrder(@RequestBody BuyOrderDto buyOrderDto) {
        String email = getUserEmail();
        tradingService.createBuyOrder(buyOrderDto, email);
        return ResponseEntity.ok(new NftResponse<>("OTP sent", null));
    }


    private static String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
