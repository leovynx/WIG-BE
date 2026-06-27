package com.whereitgo.exceptionHandling;

public class TransactionExceptions {

    public static class SMSParsingException
            extends RuntimeException {

        public SMSParsingException(String message) {
            super(message);
        }
    }

    public static class UnsupportedBankException
            extends RuntimeException {

        public UnsupportedBankException(String message) {
            super(message);
        }
    }

    public static class InvalidTransactionAmountException
            extends RuntimeException {

        public InvalidTransactionAmountException(String message) {
            super(message);
        }
    }

    public static class InvalidTransactionDateException
            extends RuntimeException {

        public InvalidTransactionDateException(String message) {
            super(message);
        }
    }
}
