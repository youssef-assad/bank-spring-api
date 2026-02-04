package com.javaapp.api_banking.service.impl;

import com.javaapp.api_banking.entity.RefreshToken;
import com.javaapp.api_banking.entity.User;
import com.javaapp.api_banking.exception.BusinessException;
import com.javaapp.api_banking.repository.RefreshTokenRepository;
import com.javaapp.api_banking.service.RefreshTokenService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service

public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${app.jwt-refresh-expiration-hours}")
    private Long refreshTokenDurationHours;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(refreshTokenDurationHours))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(
                        "REFRESH_TOKEN_NOT_FOUND",
                        "Refresh token not found"
                ));

        if (refreshToken.isRevoked()) {
            throw new BusinessException("REFRESH_TOKEN_REVOKED", "Token revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("REFRESH_TOKEN_EXPIRED", "Token expired");
        }

        return refreshToken;
    }

    @Override
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("REFRESH_TOKEN_NOT_FOUND", "Refresh token not found"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);


    }

    @Override
    public void revokeAllTokensForUser(User user) {
        refreshTokenRepository.findAllByUserAndRevokedFalseAndExpiryDateAfter(user, LocalDateTime.now())
                .forEach(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
