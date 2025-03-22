package com.nft.app.repository;

import com.nft.app.entity.InvestmentType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentTypeRepository extends MongoRepository<InvestmentType, String> {
}