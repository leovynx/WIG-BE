package com.whereitgo.exceptionHandling;

public class DashboardExceptions {

    // -------------------- Dashboard Exceptions --------------------

    public static class DashboardTransactionNotFoundException extends RuntimeException {

        public DashboardTransactionNotFoundException(String message) {
            super(message);
        }
    }

    public static class DashboardFetchException extends RuntimeException {

        public DashboardFetchException(String message) {
            super(message);
        }
    }

}
