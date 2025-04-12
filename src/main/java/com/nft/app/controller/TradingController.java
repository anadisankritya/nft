package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.BuyOrderRequest;
import com.nft.app.dto.request.SellOrderRequest;
import com.nft.app.dto.response.BuyOrderResponse;
import com.nft.app.dto.response.PageResponse;
import com.nft.app.service.TradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/api/v1/order/ongoing")
    public ResponseEntity<NftResponse<PageResponse<List<BuyOrderResponse>>>> getAllOngoingTask(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
    ) {
        String email = getUserEmail();
        PageResponse<List<BuyOrderResponse>> response = tradingService.getAllOngoingTask(page, size, email);
        return ResponseEntity.ok(new NftResponse<>("Order buy successfully", response));
    }

    @GetMapping("/api/v1/order/completed")
    public ResponseEntity<NftResponse<PageResponse<List<BuyOrderResponse>>>> getAllCompletedTask(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
    ) {
        String email = getUserEmail();
        PageResponse<List<BuyOrderResponse>> response = tradingService.getAllCompletedTask(page, size, email);
        return ResponseEntity.ok(new NftResponse<>("Order buy successfully", response));
    }


    private static String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
