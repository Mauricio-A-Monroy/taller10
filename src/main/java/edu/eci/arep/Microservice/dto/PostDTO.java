package edu.eci.arep.Microservice.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PostDTO {

    @Size(max = 140, message = "Content must be 140 characters or less")
    private String content;
    private String user;
    private LocalDate date;

    public PostDTO(){}

    public PostDTO(String user, String content, LocalDate date){
        this.user = user;
        this.content = content;
        this.date = date;
    }

    private void setContent(String content){
        this.content = content;
    }

    private void setUser(String user){
        this.user = user;
    }

    private void setDate(LocalDate date){
        this.date = date;
    }

    public String getContent(){
        return content;
    }

    public String getUser(){
        return user;
    }

    public LocalDate getDate(){
        return date;
    }
}