package com.javaapp.api_banking.controller.admin;

import com.javaapp.api_banking.Dtos.accounts.AccountResponse;
import com.javaapp.api_banking.Dtos.accounts.CreateAccountRequest;

import com.javaapp.api_banking.Dtos.admin.AdminRequest;
import com.javaapp.api_banking.Dtos.admin.AdminResponse;
import com.javaapp.api_banking.Dtos.transaction.TransactionResponse;
import com.javaapp.api_banking.Dtos.users.UserResponse;
import com.javaapp.api_banking.entity.*;

import com.javaapp.api_banking.service.AccountService;
import com.javaapp.api_banking.service.impl.AdminServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.javaapp.api_banking.service.LogService;
import com.javaapp.api_banking.Dtos.transaction.LogResponse;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminServiceImpl adminService;
    private final LogService logService;
    private final AccountService accountService;

    public AdminController(AdminServiceImpl adminService, LogService logService, AccountService accountService) {
        this.adminService = adminService;
        this.logService = logService;
        this.accountService = accountService;
    }
    @GetMapping("adminTest")
    public String test(){
        return "test admin";
    }

    @PostMapping("/createAccount")
    public AccountResponse createAccount(
            @RequestBody CreateAccountRequest request
    ) {
        return adminService.createAccount(request);
    }

    @PutMapping("/users/{userId}/status")
    public AdminResponse change_user_status(
            @PathVariable Long userId,
            @RequestParam UserStatus status
    ) {
        return adminService.change_user_status(userId, status);
    }
    @PutMapping("/accounts/{userId}/status")
    public AccountResponse change_account_status(
            @PathVariable Long userId,
            @RequestParam AccountStatus status
    ) {
        return adminService.change_account_status(userId, status);
    }
    @PutMapping("/user/{id}")
    public AdminResponse update_user(
            @PathVariable Long id,
            @RequestBody AdminRequest request
            )
    {
        return adminService.update_user(id,request);
    }
    @GetMapping("/logs")
    public Page<LogResponse> getLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            Pageable pageable
    ) {
        return logService.searchLogs(userId, action, fromDate, toDate, pageable);
    }
    @GetMapping("/transactions")
    public Page<TransactionResponse> getTransactions(
           @RequestParam(required = false) TransactionType type,
           @RequestParam(required = false) BigDecimal amount,
           @RequestParam(required = false) Account from,
           @RequestParam(required = false) Account     to,
           @RequestParam(required = false) LocalDateTime  createdAt
    , Pageable pageable
           ){
        return accountService.getAllTransactions(
                type,amount,from,to,createdAt,pageable
        );
    }

    @GetMapping("/listAccounts")
    public Page<AccountResponse> getAccounts(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) AccountStatus status,
            @RequestParam(required = false) BigDecimal balance,
            @RequestParam(required = false) LocalDateTime openedFrom,
            @RequestParam(required = false) LocalDateTime openedTo,
            @RequestParam(required = false) User user,
            Pageable pageable
    ) {
        return accountService.getAllAccounts(
                accountNumber,
                status,
                balance,
                user,
                openedFrom,
                openedTo,
                pageable
        );

    }


}
