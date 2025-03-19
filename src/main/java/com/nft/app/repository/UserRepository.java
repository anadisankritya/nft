package com.nft.app.repository;

import com.nft.app.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByEmail(String email);

  boolean existsByUserCode(String userCode);

  List<User> findByReferralCodeOrderByCreatedDateDesc(String referralCode);

  boolean existsByUsername(String username);

  boolean existsByPhoneNo(String phoneNo);

}
