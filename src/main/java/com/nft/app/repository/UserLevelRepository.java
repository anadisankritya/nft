package com.nft.app.repository;

import com.nft.app.entity.InvestmentType;
import com.nft.app.entity.UserLevel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLevelRepository extends MongoRepository<UserLevel, String> {
    Optional<UserLevel> findByName(String name);

    Optional<UserLevel> findByCheckSum(String checkSum);

    List<UserLevel> findByIdIn(List<String> ids);
}