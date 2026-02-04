package com.javaapp.api_banking.controller.account;

import com.javaapp.api_banking.Dtos.accounts.AccountResponse;
import com.javaapp.api_banking.Dtos.accounts.CreditDebitRequest;
import com.javaapp.api_banking.Dtos.transaction.TransferRequest;
import com.javaapp.api_banking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @Operation(summary = "Débiter un montant d'un compte utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant débité avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou solde insuffisant"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    @PostMapping("debit")
    public AccountResponse debitAccount(
            @RequestBody CreditDebitRequest request
            )
    {
        return accountService.debitAccount(request);
    }
    @Operation(summary = "Créditer un montant sur un compte utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant crédité avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    @PostMapping("/credit")
    public AccountResponse creditAccount(
            @RequestBody CreditDebitRequest request
    )
    {
        return accountService.creditAccount(request);
    }
    @Operation(summary = "Transférer de l'argent entre deux comptes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfert effectué avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou solde insuffisant"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })

    @PostMapping("/transfer")
    public AccountResponse transferAccount(
            @RequestBody TransferRequest request
    )
    {
        return accountService.transfer(request);
    }



}
