package com.javaapp.api_banking.service;


import com.javaapp.api_banking.Dtos.transaction.LogResponse;
import com.javaapp.api_banking.Utilis.specification.LogSpecification;
import com.javaapp.api_banking.entity.Log;
import com.javaapp.api_banking.entity.User;
import com.javaapp.api_banking.repository.LogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
    public void log(
            String action,
            String description,
            User performedBy
    )
    {
        Log log = Log.builder()
                .action(action)
                .description(description)
                .performedBy(performedBy)
                .build();
   logRepository.save(log);
    }
    public List<LogResponse> allLogs(){
        List<Log> logs = logRepository.findAll();

        return logs.stream()
                .map(
                        log -> LogResponse.builder()
                                .id(log.getId())
                                .action(log.getAction())
                                .description(log.getDescription())
                                .performedBy(
                                        log.getPerformedBy()!=null
                                        ? log.getPerformedBy().getId()
                                                :null
                                )
                                .createdAt(log.getCreatedAt())
                                .build()
                ).toList();
    }

    public Page<LogResponse> searchLogs(
            Long userId,
            String action,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable
    ) {

        Specification<Log> spec = Specification
                .where(LogSpecification.hasUser(userId))
                .and(LogSpecification.hasAction(action))
                .and(LogSpecification.fromDate(fromDate))
                .and(LogSpecification.toDate(toDate));

        return logRepository.findAll(spec, pageable)
                .map(log -> LogResponse.builder()
                        .id(log.getId())
                        .action(log.getAction())
                        .description(log.getDescription())
                        .performedBy(
                                log.getPerformedBy() != null
                                        ? log.getPerformedBy().getId()
                                        : null
                        )
                        .createdAt(log.getCreatedAt())
                        .build()
                );
    }


}
