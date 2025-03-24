package com.nft.app.repository;

import com.nft.app.entity.NFTDetails;
import com.nft.app.entity.UserLevel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NFTRepository extends MongoRepository<NFTDetails, String> {
    Optional<NFTDetails> findByName(String name);

    Optional<NFTDetails> findByCheckSum(String checkSum);

    List<NFTDetails> findByIdIn(List<String> ids);
}