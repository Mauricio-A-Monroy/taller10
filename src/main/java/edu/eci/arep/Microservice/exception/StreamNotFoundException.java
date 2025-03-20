package edu.eci.arep.Microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StreamNotFoundException extends ResponseStatusException {
    public StreamNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, "Stream with ID " + id + " not found. Please check the ID and try again.");
    }
}