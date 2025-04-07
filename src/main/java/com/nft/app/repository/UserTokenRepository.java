package com.nft.app.repository;

import com.nft.app.entity.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTokenRepository extends MongoRepository<UserToken, String> {

  List<UserToken> findByToken(String token);

  List<UserToken> findByEmailAndActive(String email, boolean active);

}
