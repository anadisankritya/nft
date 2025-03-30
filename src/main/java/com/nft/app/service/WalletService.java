package com.nft.app.service;

import com.nft.app.dto.request.FundDepositRequest;
import com.nft.app.entity.DepositRequest;
import com.nft.app.entity.User;
import com.nft.app.entity.UserWallet;
import com.nft.app.entity.WalletMaster;
import com.nft.app.entity.WithdrawRequest;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import com.nft.app.repository.DepositRequestRepository;
import com.nft.app.repository.TransactionRecordRepository;
import com.nft.app.repository.UserWalletRepository;
import com.nft.app.repository.WalletMasterRepository;
import com.nft.app.repository.WithdrawRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

  private final UserWalletRepository userWalletRepository;
  private final DepositRequestRepository depositRequestRepository;
  private final WalletMasterRepository walletMasterRepository;
  private final WithdrawRequestRepository withdrawRequestRepository;
  private final UserService userService;
  private final TransactionRecordRepository transactionRecordRepository;

  public void depositFund(String email, FundDepositRequest fundDepositRequest) {
    log.info("inside WalletService::depositFund for email - {}, amount - {}, tranId - {}",
        email, fundDepositRequest.amount(), fundDepositRequest.transactionId());

    User user = userService.getUser(email);
    Optional<WalletMaster> walletMasterOptional = walletMasterRepository.findById(user.getWalletId());
    if (walletMasterOptional.isEmpty()) {
      throw new NftException(ErrorCode.WALLET_NOT_FOUND);
    }
    boolean transactionIdUsed = depositRequestRepository.existsByStatusAndTransactionId("SUCCESS", fundDepositRequest.transactionId());
    if (transactionIdUsed) {
      throw new NftException(ErrorCode.TRANSACTION_ID_ALREADY_PRESENT);
    }
    String walletName = walletMasterOptional.get().getWalletName();
    DepositRequest depositRequest = new DepositRequest(user.getEmail(), walletName, fundDepositRequest);
    depositRequestRepository.save(depositRequest);
  }

  public void withdrawFund(String email, Integer amount) {
    log.info("inside WalletService::withdrawFund for email - {}, amount - {}", email, amount);
    User user = userService.getUser(email);

    UserWallet userWallet = getUserWallet(email);
    validateWithdrawRequest(email, amount, user, userWallet);

    Double currentBalance = userWallet.getBalance();
    WithdrawRequest withdrawRequest = new WithdrawRequest(email, amount);
    double newBalance = currentBalance - withdrawRequest.getTotalAmount();
    userWallet.setBalance(newBalance);
    log.info("Email {}, current balance - {}, new balance - {}",
        email, currentBalance, newBalance);

    userWalletRepository.save(userWallet);
    withdrawRequestRepository.save(withdrawRequest);
  }

  private void validateWithdrawRequest(String email, Integer amount, User user, UserWallet userWallet) {
    if (amount < 50 || amount > 999) {
      throw new NftException(ErrorCode.WITHDRAW_AMOUNT_ERROR, 50, 9999);
    }

    if (user.getCreatedDate().isAfter(LocalDateTime.now()
        .minusDays(UserService.appConfig.getMinWithdrawDays()))) {
      throw new NftException(ErrorCode.NEW_USER_WITHDRAW);
    }

    List<WithdrawRequest> withdrawRequestList = withdrawRequestRepository.findByEmailAndStatus(email, "PENDING");
    if (!withdrawRequestList.isEmpty()) {
      throw new NftException(ErrorCode.PENDING_WITHDRAW_REQUEST);
    }

    if (userWallet.getBalance() < amount) {
      throw new NftException(ErrorCode.INSUFFICIENT_FUNDS);
    }
  }

  public Page<?> getWithdrawalRequests(List<String> statusList, Pageable pageable) {
    if (statusList.contains("PENDING")) {
      return withdrawRequestRepository.findByStatusIn(statusList, pageable);
    }

    return withdrawRequestRepository.findByStatusInOrderByUpdatedDateDesc(statusList, pageable);
  }

  public void updateWithdrawalRequest(String id, String status, String comment) {
    Optional<WithdrawRequest> withdrawRequestOptional = withdrawRequestRepository.findById(id);
    if (withdrawRequestOptional.isPresent()) {
      final WithdrawRequest withdrawRequest = withdrawRequestOptional.get();
      switch (status) {
        case "SUCCESS" -> updateWithdrawRequest(status, comment, withdrawRequest);
        case "FAILED" -> {
          updateWithdrawRequest(status, comment, withdrawRequest);
          updateWallet(withdrawRequest.getEmail(), withdrawRequest.getTotalAmount(), "WITHDRAW_FAILED");
        }
        default -> throw new NftException(ErrorCode.INVALID_REQUEST);
      }
    }
  }

  private void updateWithdrawRequest(String status, String comment, WithdrawRequest withdrawRequest) {
    withdrawRequest.setStatus(status);
    withdrawRequest.setComment(comment);
    withdrawRequestRepository.save(withdrawRequest);
  }

  public Page<?> getDepositRequests(List<String> statusList, Pageable pageable) {
    if (statusList.contains("PENDING")) {
      return depositRequestRepository.findByStatusIn(statusList, pageable);
    }
    return depositRequestRepository.findByStatusInOrderByUpdatedDateDesc(statusList, pageable);
  }

  public void updateDepositRequest(String id, String status, String comment) {
    Optional<DepositRequest> depositRequestOptional = depositRequestRepository.findById(id);
    if (depositRequestOptional.isPresent()) {
      final DepositRequest depositRequest = depositRequestOptional.get();
      switch (status) {
        case "SUCCESS" -> {
          updateDepositRequest(status, comment, depositRequest);
          updateWallet(depositRequest.getEmail(), depositRequest.getAmount().doubleValue(), "DEPOSIT");
        }
        case "FAILED" -> updateDepositRequest(status, comment, depositRequest);
        default -> throw new NftException(ErrorCode.INVALID_REQUEST);
      }
    }
  }

  public void updateWallet(String email, Double amount, String type) {
    UserWallet userWallet = getUserWallet(email);
    userWallet.setBalance(userWallet.getBalance() + amount);
    userWalletRepository.save(userWallet);
  }

  UserWallet getUserWallet(String email) {
    return userWalletRepository.findByEmail(email);
  }

  private void updateDepositRequest(String status, String comment, DepositRequest depositRequest) {
    depositRequest.setStatus(status);
    depositRequest.setComment(comment);
    depositRequestRepository.save(depositRequest);
  }

}
