package edu.eci.arep.Microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import edu.eci.arep.Microservice.dto.PostDTO;
import java.time.LocalDate;

@Document(collection = "posts")
public class Post {

    @Id
    private String id;
    private String user;
    private String content;
    private LocalDate date;
    private String streamId; // Nuevo campo para relacionar con el stream

    public Post(PostDTO post){
        this.content = post.getContent();
        this.user = post.getUser();
        this.date = post.getDate();
    }

    public Post(){}
    
    public String getContent(){
        return content;
    }

    public String getUser(){
        return user;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStreamId(String id) {
    }
}
