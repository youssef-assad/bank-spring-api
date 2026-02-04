package com.javaapp.api_banking.Dtos.users;

import com.javaapp.api_banking.entity.Role;
import com.javaapp.api_banking.entity.UserStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

}
