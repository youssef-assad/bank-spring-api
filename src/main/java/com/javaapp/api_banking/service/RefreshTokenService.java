package com.javaapp.api_banking.service;

import com.javaapp.api_banking.entity.RefreshToken;
import com.javaapp.api_banking.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    String createRefreshToken(User user);

    RefreshToken validateRefreshToken(String token);

    void revokeRefreshToken(String token);

    void revokeAllTokensForUser(User user);

    Optional<RefreshToken> findByToken(String token);
}
