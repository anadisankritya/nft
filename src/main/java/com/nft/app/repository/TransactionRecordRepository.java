package com.nft.app.repository;

import com.nft.app.entity.TransactionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRecordRepository extends MongoRepository<TransactionRecord, String> {

}
