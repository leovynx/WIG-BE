package com.whereitgo.exceptionHandling;

public class TransactionExceptions {

    // -------------------- Parsing Exceptions --------------------

    public static class SMSParsingException extends RuntimeException {

        public SMSParsingException(String message) {
            super(message);
        }
    }

    public static class UnsupportedBankException extends RuntimeException {

        public UnsupportedBankException(String message) {
            super(message);
        }
    }

    public static class InvalidTransactionAmountException extends RuntimeException {

        public InvalidTransactionAmountException(String message) {
            super(message);
        }
    }

    public static class InvalidTransactionDateException extends RuntimeException {

        public InvalidTransactionDateException(String message) {
            super(message);
        }
    }

    // -------------------- Transaction Exceptions --------------------

    public static class TransactionNotFoundException extends RuntimeException {

        public TransactionNotFoundException(String message) {
            super(message);
        }
    }

    public static class TransactionAccessDeniedException extends RuntimeException {

        public TransactionAccessDeniedException(String message) {
            super(message);
        }
    }

    public static class TransactionAlreadyConfirmedException extends RuntimeException {

        public TransactionAlreadyConfirmedException(String message) {
            super(message);
        }
    }

    public static class InvalidTransactionNotesException extends RuntimeException {

        public InvalidTransactionNotesException(String message) {
            super(message);
        }
    }

    public static class TransactionUpdateException extends RuntimeException {

        public TransactionUpdateException(String message) {
            super(message);
        }
    }

    public static class TransactionDeleteException extends RuntimeException {

        public TransactionDeleteException(String message) {
            super(message);
        }
    }
}