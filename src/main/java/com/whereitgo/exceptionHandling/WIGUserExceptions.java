package com.whereitgo.exceptionHandling;

public class WIGUserExceptions {

     public static class UserNotFoundException
            extends RuntimeException {

        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class EmailAlreadyExistsException
            extends RuntimeException {

        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class EmailNotFoundException
            extends RuntimeException {

        public EmailNotFoundException(String message) {
            super(message);
        }
    }

}
