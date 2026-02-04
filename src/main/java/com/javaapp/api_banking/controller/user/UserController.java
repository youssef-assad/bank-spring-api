package com.javaapp.api_banking.controller.user;

import com.javaapp.api_banking.Dtos.users.UserRequest;
import com.javaapp.api_banking.Dtos.users.UserResponse;
import com.javaapp.api_banking.entity.User;
import com.javaapp.api_banking.service.UserService;
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

    @PutMapping("/me")
    public UserResponse updateMyProfile(
            @AuthenticationPrincipal User user,
          @Valid @RequestBody UserRequest request
    ) {
        return userService.updateMyProfile(user, request);
    }
    @GetMapping("/usertest")
    public String testUser(){
        return "User Test ";
    }
    @GetMapping("/me")
    public UserResponse getMyProfile(
            @AuthenticationPrincipal User user
    ) {
        return userService.getMyProfile(user);
    }

}
