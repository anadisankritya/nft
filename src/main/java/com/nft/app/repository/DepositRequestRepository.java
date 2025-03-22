package com.nft.app.repository;

import com.nft.app.entity.DepositRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRequestRepository extends MongoRepository<DepositRequest, String> {

}
