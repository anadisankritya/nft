package com.nft.app.repository;

import com.nft.app.entity.DepositRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRequestRepository extends MongoRepository<DepositRequest, String> {

  List<DepositRequest> findByStatus(String status, Pageable pageable);
}
