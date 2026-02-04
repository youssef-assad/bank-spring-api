package com.javaapp.api_banking.Dtos.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
