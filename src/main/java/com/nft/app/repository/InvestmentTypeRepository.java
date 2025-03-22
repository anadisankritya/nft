package com.nft.app.repository;

import com.nft.app.entity.InvestmentType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentTypeRepository extends MongoRepository<InvestmentType, String> {
    Optional<InvestmentType> findByName(String name);
}