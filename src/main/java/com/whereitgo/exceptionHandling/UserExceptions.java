package com.whereitgo.exceptionHandling;

public class UserExceptions {

    public static class UserNotFoundException
            extends RuntimeException {

        public UserNotFoundException(String message) {
            super(message);
        }
    }

     public static class InvalidCredentialsException
            extends RuntimeException {

        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class EmailAlreadyExistsException
            extends RuntimeException {

        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UserNameAlreadyExistsException
            extends RuntimeException {

        public UserNameAlreadyExistsException(String message) {
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
