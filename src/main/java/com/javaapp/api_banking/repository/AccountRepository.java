package com.javaapp.api_banking.repository;

import com.javaapp.api_banking.entity.Account;
import com.javaapp.api_banking.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> , JpaSpecificationExecutor<Account> {
    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByUserId(Long userId);

    boolean existsByUserIdAndStatus(Long userId, AccountStatus status);
}
