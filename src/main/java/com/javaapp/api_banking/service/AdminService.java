package com.javaapp.api_banking.service;

import com.javaapp.api_banking.Dtos.accounts.AccountResponse;
import com.javaapp.api_banking.Dtos.accounts.CreateAccountRequest;
import com.javaapp.api_banking.Dtos.admin.AdminRequest;
import com.javaapp.api_banking.Dtos.admin.AdminResponse;
import com.javaapp.api_banking.Dtos.users.UserRequest;
import com.javaapp.api_banking.Dtos.users.UserResponse;
import com.javaapp.api_banking.entity.AccountStatus;
import com.javaapp.api_banking.entity.UserStatus;

public interface AdminService {
    AccountResponse createAccount(CreateAccountRequest request) ;
    AdminResponse change_user_status(Long userId, UserStatus status);
    AccountResponse change_account_status(Long accountId, AccountStatus status);
    AdminResponse update_user(Long id, AdminRequest request);

}
