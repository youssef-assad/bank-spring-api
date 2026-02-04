package com.javaapp.api_banking.repository;

import com.javaapp.api_banking.entity.RefreshToken;
import com.javaapp.api_banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    List<RefreshToken> findAllByUserAndRevokedFalseAndExpiryDateAfter(User user, java.time.LocalDateTime now);
    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

}
