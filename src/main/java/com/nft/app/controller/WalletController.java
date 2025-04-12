package com.nft.app.controller;

import com.nft.app.dto.NftResponse;
import com.nft.app.dto.request.FundDepositRequest;
import com.nft.app.dto.response.UserDetails;
import com.nft.app.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

  private final WalletService walletService;

  @GetMapping("/api/v1/user-wallet")
  public ResponseEntity<NftResponse<UserDetails.WalletDetails>> depositRequests() {
    String email = getUserEmail();
    var userWalletDetails = walletService.getUserWalletDetails(email);
    return ResponseEntity.ok(new NftResponse<>("User wallet fetched", userWalletDetails));
  }

  @PostMapping("/api/v1/deposit")
  public ResponseEntity<NftResponse<Void>> deposit(@RequestBody FundDepositRequest fundDepositRequest) {
    String email = getUserEmail();
    walletService.depositFund(email, fundDepositRequest);
    return ResponseEntity.ok(new NftResponse<>("Deposit request sent", null));
  }

  @PostMapping("/api/v1/withdraw")
  public ResponseEntity<NftResponse<Void>> deposit(@RequestParam(defaultValue = "0") Integer amount,
                                                   @RequestParam(required = false) String otp) {
    String email = getUserEmail();
    walletService.withdrawFund(email, amount, otp);
    return ResponseEntity.ok(new NftResponse<>("Withdraw request sent, " +
        "your money will be credited in 72 hours", null));
  }

  @GetMapping("/api/v1/withdrawal-request")
  public ResponseEntity<NftResponse<Page<?>>> withdrawalRequests(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                 @RequestParam List<String> status) {
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    var list = walletService.getWithdrawalRequests(status, pageable);
    return ResponseEntity.ok(new NftResponse<>(list));
  }

  @GetMapping("/api/v1/withdrawal-history")
  public ResponseEntity<NftResponse<Page<?>>> withdrawalHistory(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                                                @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                @RequestParam(required = false) List<String> status) {
    String email = getUserEmail();
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    var list = walletService.getWithdrawalHistory(email, status, pageable);
    return ResponseEntity.ok(new NftResponse<>(list));
  }

  @PostMapping("/api/v1/withdrawal-action")
  public ResponseEntity<NftResponse<Void>> updateWithdrawal(@RequestParam String id,
                                                            @RequestParam String status,
                                                            @RequestParam String comment) {

    if (!StringUtils.hasText(comment)) {
      return ResponseEntity.badRequest().body(new NftResponse<>("comment is required", null));
    }
    walletService.updateWithdrawalRequest(id, status, comment);
    return ResponseEntity.ok(new NftResponse<>("done", null));
  }

  @GetMapping("/api/v1/deposit-request")
  public ResponseEntity<NftResponse<Page<?>>> depositRequests(@RequestParam(required = false, defaultValue = "0") Integer pageNO,
                                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                              @RequestParam List<String> status) {
    Pageable pageable = PageRequest.of(pageNO, pageSize);
    var list = walletService.getDepositRequests(status, pageable);
    return ResponseEntity.ok(new NftResponse<>(list));
  }

  @GetMapping("/api/v1/deposit-history")
  public ResponseEntity<NftResponse<Page<?>>> depositHistory(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                                             @RequestParam(required = false, defaultValue = "0") Integer pageSize,
                                                             @RequestParam(required = false) List<String> status) {
    String email = getUserEmail();
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    var list = walletService.getDepositHistory(email, status, pageable);
    return ResponseEntity.ok(new NftResponse<>(list));
  }

  @PostMapping("/api/v1/deposit-action")
  public ResponseEntity<NftResponse<Void>> updateDeposit(@RequestParam String id,
                                                         @RequestParam String status,
                                                         @RequestParam String comment) {

    if (!StringUtils.hasText(comment)) {
      return ResponseEntity.badRequest().body(new NftResponse<>("comment is required", null));
    }
    walletService.updateDepositRequest(id, status, comment);
    return ResponseEntity.ok(new NftResponse<>("done", null));
  }

  private static String getUserEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
  }

}
