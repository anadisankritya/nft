package com.nft.app.repository;

import com.nft.app.entity.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends MongoRepository<UserToken, String> {

  Optional<UserToken> findByToken(String token);

  Optional<UserToken> findTopByEmailOrderByIdDesc(String email);

  List<UserToken> findByEmailAndActive(String email, boolean active);

}
