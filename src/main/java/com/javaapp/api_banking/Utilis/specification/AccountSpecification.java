package com.javaapp.api_banking.Utilis.specification;

import com.javaapp.api_banking.entity.Account;
import com.javaapp.api_banking.entity.AccountStatus;
import com.javaapp.api_banking.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountSpecification {
    public static Specification<Account> hasAccountNumber(String accountNumber) {
        return (root, query, cb) ->
                accountNumber == null ? null :
                        cb.equal(root.get("accountNumber"), accountNumber);
    }

    public static Specification<Account> hasStatus(AccountStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<Account> minBalance(BigDecimal balance) {
        return (root, query, cb) ->
                balance == null ? null :
                        cb.greaterThanOrEqualTo(root.get("balance"), balance);
    }

    public static Specification<Account> hasUser(User user) {
        return (root, query, cb) ->
                user == null ? null :
                        cb.equal(root.get("user"), user);
    }

    public static Specification<Account> openedFrom(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null :
                        cb.greaterThanOrEqualTo(root.get("openedAt"), from);
    }

    public static Specification<Account> openedTo(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null :
                        cb.lessThanOrEqualTo(root.get("openedAt"), to);
    }
}
