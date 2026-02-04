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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Renouveler le token d'accès à l'aide d'un refresh token valide")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nouveau access token et refresh token générés"),
            @ApiResponse(responseCode = "400", description = "Refresh token invalide ou expiré"),
            @ApiResponse(responseCode = "404", description = "Refresh token non trouvé")
    })
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

    @Operation(summary = "Déconnexion de l'utilisateur en révoquant son refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token révoqué"),
            @ApiResponse(responseCode = "404", description = "Refresh token non trouvé")
    })
    @PostMapping("/logout")
    public void logout(@RequestBody RefreshTokenRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
    }
    @Operation(summary = "Créer un nouvel utilisateur (inscription)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données utilisateur invalides ou email existant")
    })

    @PostMapping("/register")
    public UserResponse createUser(
            @Valid
            @RequestBody
            UserRequest request
    ) {
        return userService.createUser(request);
    }

    @Operation(summary = "Authentifier un utilisateur et générer un JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie, JWT généré"),
            @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect")
    })
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

}
