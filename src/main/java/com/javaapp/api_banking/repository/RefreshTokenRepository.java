package com.javaapp.api_banking.repository;

import com.javaapp.api_banking.entity.RefreshToken;
import com.javaapp.api_banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    // 1️⃣ Chercher un refresh token par sa valeur
    Optional<RefreshToken> findByToken(String token);

    // 2️⃣ Supprimer tous les refresh tokens d’un user (logout de tous les devices)
    void deleteByUser(User user);

    // 3️⃣ Chercher tous les tokens valides d’un user
    List<RefreshToken> findAllByUserAndRevokedFalseAndExpiryDateAfter(User user, java.time.LocalDateTime now);

    // 4️⃣ Révoquer un token spécifique
    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);
}
