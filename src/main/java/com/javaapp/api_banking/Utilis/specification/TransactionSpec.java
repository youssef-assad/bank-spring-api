package com.javaapp.api_banking.Utilis.specification;
import com.javaapp.api_banking.entity.Account;
import com.javaapp.api_banking.entity.Transaction;
import com.javaapp.api_banking.entity.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class TransactionSpec {
    public static Specification<Transaction> hasType(TransactionType type){
        return ((root, query, criteriaBuilder) ->
                type == null ? null: criteriaBuilder.equal(root.get("type"),type)
                );
    }
    public static Specification<Transaction> hasAmount(BigDecimal amount){
        return (root,query,cb)->
                amount == null ?null:
                        cb.equal(root.get("amount"),amount);
    }
    public static Specification<Transaction> hasFrom(Account from){
        return (root,query,cb)->
                from == null ?null:
                        cb.equal(root.get("from"),from);
    }

    public static Specification<Transaction> hasTo(Account to){
        return (root,query,cb)->
                to == null ?null:
                        cb.equal(root.get("to"),to);
    }
    public static Specification<Transaction> fromDate(LocalDateTime fromDate) {
        return (root,query,cb)->
                fromDate == null ?null:cb.greaterThanOrEqualTo(
                        root.get("createdAt"),fromDate
                );
    }
    public static Specification<Transaction> toDate(LocalDateTime toDate){
        return (root,query,cb)->
                toDate==null?null:
                        cb.lessThanOrEqualTo(root.get("createdAt"),toDate);
    }


}
