package edu.eci.arep.microservice.exception;

public class ApplicationInitializationException extends RuntimeException {
    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
