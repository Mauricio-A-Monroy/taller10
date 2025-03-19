package edu.eci.arep.Microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "streams")
public class Stream {

    @Id
    private String id;
    private List<Post> posts;
    private String creator;
    private LocalDate date;

    public Stream(String id, List<Post> posts, String creator, LocalDate date) {
        this.id = id;
        this.posts = posts;
        this.creator = creator;
        this.date = date;
    }

    public Stream() {
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCreator() {
        return creator;
    }

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }
}
