package com.javaapp.api_banking.Dtos.admin;

import com.javaapp.api_banking.entity.Role;
import com.javaapp.api_banking.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email

    private String email;

    @Size(min = 8)
    private String password;
    @NotBlank
    private String phoneNumber;
    private Role role;
    private UserStatus status;
}
