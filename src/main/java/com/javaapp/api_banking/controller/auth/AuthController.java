package com.javaapp.api_banking.controller.auth;

import com.javaapp.api_banking.Dtos.auth.LoginRequest;
import com.javaapp.api_banking.Dtos.auth.LoginResponse;
import com.javaapp.api_banking.Dtos.auth.RefreshTokenRequest;
import com.javaapp.api_banking.Dtos.auth.RefreshTokenResponse;
import com.javaapp.api_banking.Dtos.users.UserRequest;
import com.javaapp.api_banking.Dtos.users.UserResponse;
import com.javaapp.api_banking.entity.RefreshToken;
import com.javaapp.api_banking.entity.User;
import com.javaapp.api_banking.repository.UserRepository;
import com.javaapp.api_banking.security.JwtTokenProvider;
import com.javaapp.api_banking.service.RefreshTokenService;
import com.javaapp.api_banking.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(@RequestBody RefreshTokenRequest request) {

        RefreshToken oldToken =
                refreshTokenService.validateRefreshToken(request.getRefreshToken());

        User user = oldToken.getUser();

        // 🔥 rotation
        refreshTokenService.revokeRefreshToken(oldToken.getToken());
        String newRefreshToken = refreshTokenService.createRefreshToken(user);

        String newAccessToken =
                jwtTokenProvider.generateTokenFromUsername(user.getEmail());

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }


    @PostMapping("/logout")
    public void logout(@RequestBody RefreshTokenRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
    }


//    @PostMapping("/refresh")
//    public LoginResponse refreshToken(@RequestBody RefreshTokenRequest request) { // Utilisation du DTO
//        String token = request.refreshToken();
//
//        // 1. Valider le token (expire ? révoqué ?)
//        refreshTokenService.validateRefreshToken(token);
//
//        // 2. Récupérer l'entité et l'utilisateur associé
//        RefreshToken tokenEntity = refreshTokenService.findByToken(token)
//                .orElseThrow(() -> new BusinessException("TOKEN_NOT_FOUND", "Refresh token non trouvé"));
//
//        User user = tokenEntity.getUser();
//
//        // 3. LOGIQUE DE SÉCURITÉ : Révoquer l'ancien token avant d'en donner un nouveau
//        // Cela évite qu'un token volé soit réutilisé à l'infini
//        refreshTokenService.revokeRefreshToken(token);
//
//        // 4. Générer le nouveau couple de tokens
//        String newJwt = jwtTokenProvider.generateTokenFromUsername(user.getEmail());
//        String newRefreshToken = refreshTokenService.createRefreshToken(user);
//
//        return LoginResponse.builder()
//                .email(user.getEmail())
//                .jwt(newJwt)
//                .refreshToken(newRefreshToken)
//                .name(user.getFirstName() + " " + user.getLastName())
//                .build();
//    }

    @PostMapping("/register")
    public UserResponse createUser(
            @Valid
            @RequestBody
            UserRequest request
    ) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

}
