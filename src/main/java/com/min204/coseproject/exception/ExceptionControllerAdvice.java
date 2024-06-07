package com.min204.coseproject.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("success", false);
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("data", Map.of(
                "email", exception.getEmail(),
                "isAvailable", false,
                "Type", exception.getType()
        ));

        return ResponseEntity.badRequest().body(errorDetails);
    }
}