package com.javaapp.api_banking.service;

import com.javaapp.api_banking.Dtos.auth.LoginRequest;
import com.javaapp.api_banking.Dtos.auth.LoginResponse;
import com.javaapp.api_banking.Dtos.users.UserRequest;
import com.javaapp.api_banking.Dtos.users.UserResponse;
import com.javaapp.api_banking.entity.User;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    LoginResponse login(LoginRequest request);

    UserResponse getMyProfile(User user);

    UserResponse updateMyProfile(User user, UserRequest request);
}
