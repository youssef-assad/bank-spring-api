package com.javaapp.api_banking.Dtos.accounts;

import com.javaapp.api_banking.entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AccountResponse {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountStatus status;
}
