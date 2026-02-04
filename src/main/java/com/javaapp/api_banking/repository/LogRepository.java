package com.javaapp.api_banking.repository;
import com.javaapp.api_banking.entity.Log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository

public interface LogRepository
        extends JpaRepository<Log, Long>,
        JpaSpecificationExecutor<Log> {


}

