package com.whereitgo.exceptionHandling;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

        // -------------------- Common Error Response --------------------

        private ResponseEntity<Object> buildErrorResponse(
                        HttpStatus status,
                        String error,
                        String message) {

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", status.value());
                response.put("error", error);
                response.put("message", message);

                return new ResponseEntity<>(response, status);
        }

        // -------------------- User Exceptions --------------------

        @ExceptionHandler(UserExceptions.UserNotFoundException.class)
        public ResponseEntity<Object> handleUserNotFound(
                        UserExceptions.UserNotFoundException ex) {

                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                "NOT_FOUND",
                                ex.getMessage());
        }

        @ExceptionHandler(UserExceptions.EmailAlreadyExistsException.class)
        public ResponseEntity<Object> handleEmailExists(
                        UserExceptions.EmailAlreadyExistsException ex) {

                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                "CONFLICT",
                                ex.getMessage());
        }

        @ExceptionHandler(UserExceptions.UserNameAlreadyExistsException.class)
        public ResponseEntity<Object> handleUserExists(
                        UserExceptions.UserNameAlreadyExistsException ex) {

                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                "CONFLICT",
                                ex.getMessage());
        }

        @ExceptionHandler(UserExceptions.EmailNotFoundException.class)
        public ResponseEntity<Object> handleEmailNotFound(
                        UserExceptions.EmailNotFoundException ex) {

                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                "NOT_FOUND",
                                ex.getMessage());
        }

        @ExceptionHandler(UserExceptions.InvalidCredentialsException.class)
        public ResponseEntity<Object> handleInvalidCredentials(
                        UserExceptions.InvalidCredentialsException ex) {

                return buildErrorResponse(
                                HttpStatus.UNAUTHORIZED,
                                "NOT_FOUND",
                                ex.getMessage());
        }

        // -------------------- Transaction Exceptions --------------------

        @ExceptionHandler(TransactionExceptions.SMSParsingException.class)
        public ResponseEntity<Object> handleSMSParsingException(
                        TransactionExceptions.SMSParsingException ex) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "SMS_PARSING_ERROR",
                                ex.getMessage());
        }

        @ExceptionHandler(TransactionExceptions.UnsupportedBankException.class)
        public ResponseEntity<Object> handleUnsupportedBankException(
                        TransactionExceptions.UnsupportedBankException ex) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "UNSUPPORTED_BANK",
                                ex.getMessage());
        }

        @ExceptionHandler(TransactionExceptions.InvalidTransactionAmountException.class)
        public ResponseEntity<Object> handleInvalidAmountException(
                        TransactionExceptions.InvalidTransactionAmountException ex) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "INVALID_TRANSACTION_AMOUNT",
                                ex.getMessage());
        }

        @ExceptionHandler(TransactionExceptions.InvalidTransactionDateException.class)
        public ResponseEntity<Object> handleInvalidDateException(
                        TransactionExceptions.InvalidTransactionDateException ex) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "INVALID_TRANSACTION_DATE",
                                ex.getMessage());
        }

        @ExceptionHandler(TransactionExceptions.TransactionNotFoundException.class)
        public ResponseEntity<Object> handleTransactionNotFound(
                        TransactionExceptions.TransactionNotFoundException ex) {

                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                "TRANSACTION_NOT_FOUND",
                                ex.getMessage());
        }

        @ExceptionHandler(TransactionExceptions.TransactionAccessDeniedException.class)
        public ResponseEntity<Object> handleTransactionAccessDenied(
                        TransactionExceptions.TransactionAccessDeniedException ex) {

                return buildErrorResponse(
                                HttpStatus.FORBIDDEN,
                                "TRANSACTION_ACCESS_DENIED",
                                ex.getMessage());
        }

        @ExceptionHandler(TransactionExceptions.TransactionAlreadyConfirmedException.class)
        public ResponseEntity<Object> handleTransactionAlreadyConfirmed(
                        TransactionExceptions.TransactionAlreadyConfirmedException ex) {

                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                "TRANSACTION_ALREADY_CONFIRMED",
                                ex.getMessage());
        }

        @ExceptionHandler(TransactionExceptions.InvalidTransactionNotesException.class)
        public ResponseEntity<Object> handleInvalidTransactionNotes(
                        TransactionExceptions.InvalidTransactionNotesException ex) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "INVALID_TRANSACTION_NOTES",
                                ex.getMessage());
        }

        @ExceptionHandler(TransactionExceptions.TransactionUpdateException.class)
        public ResponseEntity<Object> handleTransactionUpdateException(
                        TransactionExceptions.TransactionUpdateException ex) {

                return buildErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "TRANSACTION_UPDATE_FAILED",
                                ex.getMessage());
        }

        // -------------------- Generic Exception --------------------

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleException(Exception ex) {

                return buildErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "INTERNAL_SERVER_ERROR",
                                ex.getMessage());
        }
}