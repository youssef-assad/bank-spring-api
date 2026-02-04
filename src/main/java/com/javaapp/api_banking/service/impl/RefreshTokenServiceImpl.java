package com.javaapp.api_banking.service.impl;

import com.javaapp.api_banking.entity.RefreshToken;
import com.javaapp.api_banking.entity.User;
import com.javaapp.api_banking.exception.BusinessException;
import com.javaapp.api_banking.repository.RefreshTokenRepository;
import com.javaapp.api_banking.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Long REFRESH_TOKEN_DURATION_HOURS = 24 * 30L;
    @Override
    public String createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(REFRESH_TOKEN_DURATION_HOURS))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Override
    public boolean validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(
                ()-> new BusinessException("REFRESH_TOKEN_NOT_FOUND", "Refresh token not found"));
        if(
                refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())
        ){
            throw new BusinessException("REFRESH_TOKEN_INVALID", "Refresh token is invalid or expired");
        }

        return true;
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
