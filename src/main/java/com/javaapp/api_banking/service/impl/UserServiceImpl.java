package com.javaapp.api_banking.service.impl;

import com.javaapp.api_banking.Dtos.auth.LoginRequest;
import com.javaapp.api_banking.Dtos.auth.LoginResponse;
import com.javaapp.api_banking.Dtos.users.UserRequest;
import com.javaapp.api_banking.Dtos.users.UserResponse;
import com.javaapp.api_banking.security.JwtTokenProvider;
import com.javaapp.api_banking.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.javaapp.api_banking.entity.User;
import com.javaapp.api_banking.exception.BusinessException;
import com.javaapp.api_banking.repository.UserRepository;
import com.javaapp.api_banking.service.EmailService;
import com.javaapp.api_banking.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @Transactional
    @Override
    public UserResponse createUser(UserRequest userRequest) {

        String hashPassword = passwordEncoder.encode(userRequest.getPassword());
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new BusinessException("USER_EMAIL_EXISTS",
                    "Email already exists");
        }
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(hashPassword)
                .phoneNumber(userRequest.getPhoneNumber())
                .build();
        userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }


        @Override
    @Transactional
    public LoginResponse login(LoginRequest request){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
            User user = (User) authentication.getPrincipal();
String refreshToken = refreshTokenService.createRefreshToken(user);
        return LoginResponse.builder()
                .email(request.getEmail())
                .jwt(jwtTokenProvider.generateToken(authentication))
                .refreshToken(refreshToken)
                .build();
    }
//    public LoginResponse login(LoginRequest request) {
//        // 1. Authentification standard (Spring Security)
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "Utilisateur non trouvé"));
//
//        // 2. Générer le JWT (Access Token - durée courte, ex: 15 min)
//        String jwt = jwtTokenProvider.generateTokenFromUsername(user.getEmail());
//
//        // 3. Générer le Refresh Token (Durée longue via ton nouveau service)
//        String refreshToken = refreshTokenService.createRefreshToken(user);
//
//        // 4. On renvoie tout au client
//        return LoginResponse.builder()
//                .email(user.getEmail())
//                .jwt(jwt)
//                .refreshToken(refreshToken) // <-- Très important !
//                .name(user.getFirstName() + " " + user.getLastName())
//                .build();
//    }

    @Override
    public UserResponse getMyProfile(User user) {

        // user is already authenticated & trusted
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())

                .build();
    }

    @Override
    public UserResponse updateMyProfile(User user, UserRequest request) {

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .email(updatedUser.getEmail())
                .phoneNumber(updatedUser.getPhoneNumber())

                .build();
    }
}
