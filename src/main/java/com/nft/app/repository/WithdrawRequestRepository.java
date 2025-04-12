package com.nft.app.repository;

import com.nft.app.entity.WithdrawRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawRequestRepository extends MongoRepository<WithdrawRequest, String> {

  List<WithdrawRequest> findByEmailAndStatus(String email, String status);

  Page<WithdrawRequest> findByStatusIn(List<String> statusList, Pageable pageable);

  Page<WithdrawRequest> findByStatusInOrderByUpdatedDateDesc(List<String> statusList, Pageable pageable);

  Page<WithdrawRequest> findByEmailAndStatusInOrderByIdDesc(String email, List<String> statusList, Pageable pageable);

  Page<WithdrawRequest> findByEmailOrderByIdDesc(String email, Pageable pageable);


}
