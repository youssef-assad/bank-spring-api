package com.javaapp.api_banking.service.impl;

import com.javaapp.api_banking.Dtos.accounts.AccountResponse;
import com.javaapp.api_banking.Dtos.accounts.CreateAccountRequest;
import com.javaapp.api_banking.Dtos.admin.AdminRequest;
import com.javaapp.api_banking.Dtos.admin.AdminResponse;
import com.javaapp.api_banking.Dtos.users.UserRequest;
import com.javaapp.api_banking.Dtos.users.UserResponse;
import com.javaapp.api_banking.Utilis.AccountNumberGenerator;
import com.javaapp.api_banking.entity.Account;
import com.javaapp.api_banking.entity.AccountStatus;
import com.javaapp.api_banking.entity.User;
import com.javaapp.api_banking.entity.UserStatus;
import com.javaapp.api_banking.exception.BusinessException;
import com.javaapp.api_banking.repository.AccountRepository;
import com.javaapp.api_banking.repository.UserRepository;
import com.javaapp.api_banking.service.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AdminServiceImpl implements AdminService {
    private final AccountNumberGenerator accountNumberGenerator;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AccountNumberGenerator accountNumberGenerator, UserRepository userRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountNumberGenerator = accountNumberGenerator;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        System.out.println("Looking for user...");
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found"));
        System.out.println("User found: " + user.getId());
        System.out.println("Generating account number...");
        String accountNumber = accountNumberGenerator.generateAccountNumber();
        System.out.println("Generated: " + accountNumber);
        System.out.println("Building account...");
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .user(user)
                .balance(request.getInitialBalance() == null ? BigDecimal.ZERO : request.getInitialBalance())
                .status(AccountStatus.ACTIVE)
                .build();
        System.out.println("Saving account...");
        accountRepository.save(account);
        System.out.println("Account saved!");

        if (user.getStatus() == UserStatus.PENDING) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }
@Transactional
    @Override
    public AdminResponse change_user_status(Long userId, UserStatus status) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException("USER_NOT_FOUND", "User not found")
        );
        user.setStatus(status);
        userRepository.save(user);
        return AdminResponse.builder()
                .id(user.getId())
                .status(user.getStatus())
                .email(user.getEmail())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public AccountResponse change_account_status(Long accountId, AccountStatus status) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                ()->new BusinessException("ACCOUNT_NOT_FOUND", "Account not found")
        );
        account.setStatus(status);
        accountRepository.save(account);
        return AccountResponse.builder()
                .id(account.getId())
                .status(account.getStatus())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();


    }

    @Override
    public AdminResponse update_user(Long id, AdminRequest request) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new BusinessException("USER_NOT_FOUND", "User not found in DB")
        );

        // 2️⃣ Check if email is changing and already exists
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("USER_EMAIL_EXISTS", "Email already exists");
        }

        // 3️⃣ Update fields
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        // 4️⃣ Update password only if it's not blank
        if( request.getPassword() != null && !request.getPassword().isEmpty() ){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        // 5️⃣ Update role and status (admin-only fields)
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        userRepository.save(user);
        return AdminResponse.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
