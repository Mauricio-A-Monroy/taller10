package edu.eci.arep.microservice.exception;

public class UserException extends Exception{

    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NAME_USED = "Username is already in use";
    public static final String CREATOR_REQUIRES = "Creator is required";

    public UserException(String message){
        super(message);
    }
}
