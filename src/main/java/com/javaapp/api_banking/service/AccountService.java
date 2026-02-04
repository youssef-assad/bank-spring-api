package com.javaapp.api_banking.service;

import com.javaapp.api_banking.Dtos.accounts.AccountResponse;
import com.javaapp.api_banking.Dtos.accounts.CreditDebitRequest;
import com.javaapp.api_banking.Dtos.transaction.TransactionResponse;
import com.javaapp.api_banking.Dtos.transaction.TransferRequest;
import com.javaapp.api_banking.entity.Account;
import com.javaapp.api_banking.entity.AccountStatus;
import com.javaapp.api_banking.entity.TransactionType;
import com.javaapp.api_banking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {
    AccountResponse debitAccount(CreditDebitRequest request);

    AccountResponse creditAccount(CreditDebitRequest request);

    AccountResponse transfer(TransferRequest request);

    Page<TransactionResponse> getAllTransactions(TransactionType type, BigDecimal amount, Account from, Account to, LocalDateTime createdAt, Pageable pageable);
    Page<AccountResponse> getAllAccounts(
            String accountNumber,
            AccountStatus status,
            BigDecimal balance ,
            User user,
            LocalDateTime createdAt,
            LocalDateTime closedAt,
            Pageable pageable

    );

}
