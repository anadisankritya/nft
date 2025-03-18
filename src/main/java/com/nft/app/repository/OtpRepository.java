package com.nft.app.repository;

import com.nft.app.entity.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<Otp, String> {
  Optional<Otp> findByTypeAndKey(String type, String key);
}
