package edu.eci.arep.Microservice.dto;

import java.time.LocalDate;
import java.util.List;
import edu.eci.arep.Microservice.model.Post;

public class StreamDTO {
    
    private List<Post> posts;
    private String creator;
    private LocalDate date;

    public StreamDTO(){}

    public StreamDTO(String creator, List<Post> posts, LocalDate date){
        this.creator = creator;
        this.posts = posts;
        this.date = date;
    }

    public void setCreator(String creator){
        this.creator = creator;
    }

    public void setPosts(List<Post> posts){
        this.posts = posts;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public String getCreator(){
        return creator;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public LocalDate getDate() {
        return date;
    }
}
