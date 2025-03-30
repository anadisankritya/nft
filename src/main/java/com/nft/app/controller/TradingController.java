package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.BuyOrderRequest;
import com.nft.app.dto.request.SellOrderRequest;
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
    public ResponseEntity<NftResponse<String>> createBuyOrder(@RequestBody BuyOrderRequest buyOrderRequest) {
        String email = getUserEmail();
        tradingService.createBuyOrder(buyOrderRequest, email);
        return ResponseEntity.ok(new NftResponse<>("Order buy successfully", null));
    }

    @PostMapping("/api/v1/order/sell")
    public ResponseEntity<NftResponse<String>> createSellOrder(@RequestBody SellOrderRequest sellOrderRequest) {
        String email = getUserEmail();
        tradingService.createSellOrder(sellOrderRequest, email);
        return ResponseEntity.ok(new NftResponse<>("Order Sell successfully", null));
    }


    private static String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
