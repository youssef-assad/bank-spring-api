package com.javaapp.api_banking.Dtos.transaction;

import com.javaapp.api_banking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogResponse {
    private Long id;
    private String action;
    private String description;
    private Long performedBy;
    private LocalDateTime createdAt;
}
