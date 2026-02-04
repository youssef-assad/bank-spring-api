package com.javaapp.api_banking.Utilis.specification;

import com.javaapp.api_banking.entity.Log;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class LogSpecification {
    public static Specification<Log> hasUser(Long userId){
        return ((root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("performedBy").get("id"),userId)
                );
    }
    public static Specification<Log> hasAction(String action){
        return (root,query,cb)->
                action ==null ? null
                        : cb.equal(root.get("action"),action);
    }
    public static Specification<Log> fromDate(LocalDateTime fromDate) {
        return (root,query,cb)->
                fromDate == null ?null:cb.greaterThanOrEqualTo(
                        root.get("createdAt"),fromDate
                );
    }
    public static Specification<Log> toDate(LocalDateTime toDate){
        return (root,query,cb)->
                toDate==null?null:
                        cb.lessThanOrEqualTo(root.get("createdAt"),toDate);
    }


}
