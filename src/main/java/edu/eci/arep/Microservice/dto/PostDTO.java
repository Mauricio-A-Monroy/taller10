package edu.eci.arep.Microservice.dto;

import edu.eci.arep.Microservice.model.User;

import java.time.LocalDate;
import java.util.Date;

public class PostDTO {

    private String content;
    private String user;
    private LocalDate date;

    public PostDTO(){}

    public PostDTO(String user, String content, LocalDate date) throws Exception{
        this.user = user;
        setContent(content);
        this.date = date;
    }

    private void setContent(String content) throws Exception{
        if(content.length() > 140){
            throw new Exception("very expand content");
        }
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

    public LocalDate getDate(){return date;}
    
}
