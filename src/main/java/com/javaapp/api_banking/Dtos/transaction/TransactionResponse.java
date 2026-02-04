package com.javaapp.api_banking.Dtos.transaction;

import com.javaapp.api_banking.entity.Account;
import com.javaapp.api_banking.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TransactionResponse {

    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private String from;
    private String to;
    private LocalDateTime createdAt;
}
