package com.nft.app.repository;

import com.nft.app.entity.UserWallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWalletRepository extends MongoRepository<UserWallet, String> {

  UserWallet findByEmail(String email);
}
