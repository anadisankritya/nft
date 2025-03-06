package com.nft.app.repository;

import com.nft.app.entity.EmailOtp;
import com.nft.app.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailOtpRepository extends MongoRepository<EmailOtp, String> {
  Optional<EmailOtp> findByEmail(String email);
}
