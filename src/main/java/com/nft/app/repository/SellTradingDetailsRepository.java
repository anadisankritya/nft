package com.nft.app.repository;

import com.nft.app.entity.SellTradingDetailsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellTradingDetailsRepository extends MongoRepository<SellTradingDetailsEntity, String> {
}
