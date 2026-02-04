package com.javaapp.api_banking.Dtos.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class LogResponse {
    private String action;
    private String description;
    private LocalDateTime createdAt;
}
