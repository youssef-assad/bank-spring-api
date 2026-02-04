package com.javaapp.api_banking.service.impl;

import com.javaapp.api_banking.Dtos.accounts.AccountResponse;
import com.javaapp.api_banking.Dtos.accounts.CreditDebitRequest;
import com.javaapp.api_banking.Dtos.transaction.TransactionResponse;
import com.javaapp.api_banking.Dtos.transaction.TransferRequest;
import com.javaapp.api_banking.Utilis.specification.AccountSpecification;
import com.javaapp.api_banking.Utilis.specification.TransactionSpec;
import com.javaapp.api_banking.entity.*;
import com.javaapp.api_banking.exception.BusinessException;
import com.javaapp.api_banking.repository.AccountRepository;
import com.javaapp.api_banking.repository.TransactionRepository;
import com.javaapp.api_banking.service.AccountService;
import com.javaapp.api_banking.service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LogService logService;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, LogService logService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.logService = logService;
    }


    public Account validateAccountForOperation(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException(
                        "ACCOUNT_NOT_FOUND",
                        "Account not found"
                ));

        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new BusinessException(
                    "ACCOUNT_FROZEN",
                    "Account is frozen"
            );
        }

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new BusinessException(
                    "ACCOUNT_CLOSED",
                    "Account is closed"
            );
        }

        return account;
    }


    @Override
    @Transactional
    public AccountResponse debitAccount(CreditDebitRequest request) {
        Account account = validateAccountForOperation(request.getAccountNumber());
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException(
                    "INSUFFICIENT_FUNDS",
                    "Not enough balance"
            );
        }
        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    "INVALID_AMOUNT",
                    "Transfer amount must be greater than zero"
            );
        }
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(request.getAmount())
                .fromAccount(account)
                .build();
      transactionRepository.save(transaction);
        logService.log(
                "DEBIT_ACCOUNT",
                "Debit of " + request.getAmount() +
                        " from account " + account.getAccountNumber(),
                account.getUser()
        );

        return AccountResponse.builder()
                .id(account.getId())
                .status(account.getStatus())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

    @Override
    @Transactional
    public AccountResponse creditAccount(CreditDebitRequest request) {
        Account account = validateAccountForOperation(request.getAccountNumber());
        account.setBalance(account.getBalance().add(request.getAmount()));
        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    "INVALID_AMOUNT",
                    "Transfer amount must be greater than zero"
            );
        }
        accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .fromAccount(account)
                .build();
        transactionRepository.save(transaction);
        logService.log(
                "CREDIT_ACCOUNT",
                "Credit of " + request.getAmount() +
                        " from account " + account.getAccountNumber(),
                account.getUser()
        );

        return AccountResponse.builder()
                .id(account.getId())
                .status(account.getStatus())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

    @Override
    @Transactional
    public AccountResponse transfer(TransferRequest request) {
        Account fromAccount = validateAccountForOperation(request.getFromAccountNumber());
        Account toAccount = validateAccountForOperation(request.getToAccountNumber());
        if(
                fromAccount.getId().equals(toAccount.getId())
        ){
            throw new BusinessException("SENDER_≠_RECEIVER","Sender and receiver must be different accounts");
        }
        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    "INVALID_AMOUNT",
                    "Transfer amount must be greater than zero"
            );
        }
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException(
                    "INSUFFICIENT_FUNDS",
                    "Not enough balance"
            );
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        Transaction transaction = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .build();
        transactionRepository.save(transaction);
        logService.log(
                "TRANSFER_ACCOUNT",
                "Transfer of " + request.getAmount() +
                        " from account " + request.getFromAccountNumber(),
                fromAccount.getUser()
        );
        return AccountResponse.builder()
                .id(fromAccount.getId())
                .status(fromAccount.getStatus())
                .accountNumber(fromAccount.getAccountNumber())
                .balance(fromAccount.getBalance())
                .build();
    }

    @Override
    public Page<TransactionResponse> getAllTransactions(TransactionType type, BigDecimal amount, Account from, Account to, LocalDateTime createdAt, Pageable pageable) {
        Specification<Transaction> spec = Specification.where(TransactionSpec.hasType(type))
                .and(TransactionSpec.hasAmount(amount))
                .and(TransactionSpec.hasFrom(from))
                .and(TransactionSpec.hasTo(to))
                .and(TransactionSpec.fromDate(createdAt))
                .and(TransactionSpec.toDate(createdAt));



        return transactionRepository.findAll(spec, pageable)
                .map(t->TransactionResponse.builder()
                        .id(t.getId())
                        .type(t.getType())
                        .amount(t.getAmount())
                        .from(String.valueOf(t.getFromAccount().getId()))
                        .to(String.valueOf(t.getToAccount()!=null ? t.getToAccount().getId() : null))
                        .createdAt(t.getCreatedAt())
                        .build())
                ;
    }


    @Override
    public Page<AccountResponse> getAllAccounts(String accountNumber, AccountStatus status, BigDecimal balance, User user, LocalDateTime createdAt, LocalDateTime closedAt, Pageable pageable) {
        Specification<Account> spec = Specification
                .where(AccountSpecification.hasAccountNumber(accountNumber))
                .and(AccountSpecification.hasStatus(status))
                .and(AccountSpecification.minBalance(balance))
                .and(AccountSpecification.hasUser(user))
                .and(AccountSpecification.openedFrom(createdAt))
                .and(AccountSpecification.openedTo(closedAt));
        return accountRepository.findAll(spec,pageable).map(
account -> AccountResponse.builder()
        .id(account.getId())
        .accountNumber(account.getAccountNumber())
        .balance(account.getBalance())
        .status(account.getStatus())

        .build()
        );
    }


}
