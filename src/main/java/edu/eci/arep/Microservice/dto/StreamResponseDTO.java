package edu.eci.arep.Microservice.dto;

import edu.eci.arep.Microservice.model.Post;
import java.time.LocalDate;
import java.util.List;

public class StreamResponseDTO {
    private String id;
    private String creator;
    private LocalDate date;
    private List<Post> posts;

    public StreamResponseDTO() {}

    public StreamResponseDTO(String id, String creator, LocalDate date, List<Post> posts) {
        this.id = id;
        this.creator = creator;
        this.date = date;
        this.posts = posts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
