package com.javaapp.api_banking.controller.user;

import com.javaapp.api_banking.Dtos.users.UserRequest;
import com.javaapp.api_banking.Dtos.users.UserResponse;
import com.javaapp.api_banking.entity.User;
import com.javaapp.api_banking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Mettre à jour le profil de l'utilisateur connecté")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/me")
    public UserResponse updateMyProfile(
            @AuthenticationPrincipal User user,
          @Valid @RequestBody UserRequest request
    ) {
        return userService.updateMyProfile(user, request);
    }
    @Operation(summary = "Afficher les informations du profil de l'utilisateur connecté")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil récupéré avec succès"),
            @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
    })
    @GetMapping("/me")
    public UserResponse getMyProfile(
            @AuthenticationPrincipal User user
    ) {
        return userService.getMyProfile(user);
    }

}
