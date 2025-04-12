package com.nft.app.repository;

import com.nft.app.entity.TransactionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRecordRepository extends MongoRepository<TransactionRecord, String> {

  Page<TransactionRecord> findByEmailOrderByIdDesc(String email, Pageable pageable);

}
