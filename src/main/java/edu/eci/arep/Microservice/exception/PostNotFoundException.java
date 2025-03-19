package edu.eci.arep.Microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PostNotFoundException extends ResponseStatusException {
  public PostNotFoundException(String id) {
    super(HttpStatus.NOT_FOUND, "Post with ID " + id + " not found. Please check the ID and try again.");
  }
}
