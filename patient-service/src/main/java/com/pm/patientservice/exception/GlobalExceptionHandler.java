package com.pm.patientservice.exception;

import com.pm.patientservice.dto.PatientResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePatientNotFoundException(PatientNotFoundException e) {
        log.warn("Patient not found {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Patient not found");

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        log.warn("Email already exists {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Email already exists");

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("No resource found {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "No resource found");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        log.error("Unexpected error occurred {}", e.getMessage()); // Logs full stack trace

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "An unexpected error occurred. Please contact support.");

        return ResponseEntity.status(500).body(errors); // HTTP 500 Internal Server Error
    }


}
