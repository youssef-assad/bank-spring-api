package com.javaapp.api_banking.controller.account;

import com.javaapp.api_banking.Dtos.accounts.AccountResponse;
import com.javaapp.api_banking.Dtos.accounts.CreditDebitRequest;
import com.javaapp.api_banking.Dtos.transaction.TransferRequest;
import com.javaapp.api_banking.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("debit")
    public AccountResponse debitAccount(
            @RequestBody CreditDebitRequest request
            )
    {
        return accountService.debitAccount(request);
    }
    @PostMapping("/credit")
    public AccountResponse creditAccount(
            @RequestBody CreditDebitRequest request
    )
    {
        return accountService.creditAccount(request);
    }

    @PostMapping("/transfer")
    public AccountResponse transferAccount(
            @RequestBody TransferRequest request
    )
    {
        return accountService.transfer(request);
    }



}
