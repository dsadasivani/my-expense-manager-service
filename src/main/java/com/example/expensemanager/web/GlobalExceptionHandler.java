package com.example.expensemanager.web;

import com.example.expensemanager.dto.ApiError;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req){
        var details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage()).toList();
        return ResponseEntity.badRequest().body(new ApiError(
                Instant.now(), req.getRequestURI(), "VALIDATION_ERROR", "Validation error", details
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegal(IllegalArgumentException ex, HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(
                Instant.now(), req.getRequestURI(), "CONFLICT", ex.getMessage(), List.of()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
                Instant.now(), req.getRequestURI(), "INTERNAL_ERROR", ex.getMessage(), List.of()
        ));
    }
}
