package com.javaapp.api_banking.repository;

import com.javaapp.api_banking.entity.User;
import com.javaapp.api_banking.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmailAndStatus(String email, UserStatus status);

}
