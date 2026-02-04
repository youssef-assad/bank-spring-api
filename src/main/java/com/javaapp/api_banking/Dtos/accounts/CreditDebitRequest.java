package com.javaapp.api_banking.Dtos.accounts;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditDebitRequest {
    private String accountNumber;
    private BigDecimal amount;
}
