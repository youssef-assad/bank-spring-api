package com.javaapp.api_banking.exception;

import com.javaapp.api_banking.Dtos.admin.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex){
        return ResponseEntity.badRequest().body(
                new ErrorResponse(ex.getCode(), ex.getMessage())
        );
    }
}
