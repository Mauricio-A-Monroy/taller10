package edu.eci.arep.Microservice.dto;

import jakarta.validation.constraints.Email;

public class UserDTO {

    private String name;
    private String lastName;
    @Email(message = "Invalid email")
    private String email;
    private String password;

    public UserDTO() {}

    public UserDTO(String name, String lastName, String email, String password) {
        this.name  = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public @Email(message = "Invalid email") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Invalid email") String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}