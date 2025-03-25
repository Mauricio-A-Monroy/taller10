package edu.eci.arep.microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {
    public UserNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, "User with ID " + id + " not found. Please check the ID and try again.");
    }
}