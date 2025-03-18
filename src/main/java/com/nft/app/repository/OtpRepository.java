package com.nft.app.repository;

import com.nft.app.entity.OtpDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<OtpDetails, String> {
  Optional<OtpDetails> findByTypeAndKey(String type, String key);
}
