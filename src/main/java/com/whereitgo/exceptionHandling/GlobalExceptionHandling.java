package com.whereitgo.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(WIGUserExceptions.UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(
            WIGUserExceptions.UserNotFoundException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", 404);
        error.put("error", "NOT_FOUND");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WIGUserExceptions.EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailExists(
            WIGUserExceptions.EmailAlreadyExistsException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", 409);
        error.put("error", "CONFLICT");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WIGUserExceptions.PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<?> handlePhoneExists(
            WIGUserExceptions.PhoneNumberAlreadyExistsException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", 409);
        error.put("error", "CONFLICT");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT);
    }
}