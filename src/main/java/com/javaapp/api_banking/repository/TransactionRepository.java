package com.javaapp.api_banking.repository;

import com.javaapp.api_banking.Dtos.transaction.TransactionResponse;
import com.javaapp.api_banking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long>  , JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByFromAccountId(Long accountId);

    List<Transaction> findByToAccountId(Long accountId);

    List<Transaction> findByFromAccountIdOrToAccountId(Long fromId, Long toId);


}
