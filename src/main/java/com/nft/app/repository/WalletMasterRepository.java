package com.nft.app.repository;

import com.nft.app.entity.WalletMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletMasterRepository extends MongoRepository<WalletMaster, String> {

}
