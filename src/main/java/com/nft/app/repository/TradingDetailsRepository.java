package com.nft.app.repository;

import com.nft.app.entity.TradingDetailsEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradingDetailsRepository extends MongoRepository<TradingDetailsEntity, String> {
    Optional<TradingDetailsEntity> findByCreatedByAndNftId(String userCode, @NotBlank(message = "nftId is required") String nftId);

    Optional<TradingDetailsEntity> findByCreatedByAndId(String userCode, @NotBlank(message = "orderId is required") String orderId);

    Optional<TradingDetailsEntity> findByCreatedByAndIdAndOperation(String userCode, @NotBlank(message = "orderId is required") String orderId, String buy);

    Page<TradingDetailsEntity> findByOperationAndCreatedByAndSellBlockTillGreaterThan(
            String operation, String createdBy, long sellBlockTill, Pageable pageable);

    Page<TradingDetailsEntity> findByOperationInAndCreatedByAndSellBlockTillLessThan(
            List<String> operation, String createdBy, long sellBlockTill, Pageable pageable);

}
