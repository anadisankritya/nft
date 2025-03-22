package com.nft.app.repository;

import com.nft.app.entity.WithdrawRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawRequestRepository extends MongoRepository<WithdrawRequest, String> {

  List<WithdrawRequest> findByEmailAndStatus(String email, String status);

}
