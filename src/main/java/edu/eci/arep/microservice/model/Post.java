package edu.eci.arep.microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import edu.eci.arep.microservice.dto.PostDTO;
import java.time.LocalDate;

@Document(collection = "posts")
public class Post {

    @Id
    private String id;
    private String creator;
    private String content;
    private LocalDate date;
    private String streamId; // Nuevo campo para relacionar con el stream

    public Post() {}

    public Post(PostDTO post){
        this.creator = post.getCreator();
        this.content = post.getContent();
        this.date = LocalDate.now();
        this.streamId = post.getStreamId();
    }

    public Post(String creator, String content, String streamId) {
        this.creator = creator;
        this.content = content;
        this.streamId = streamId;
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

    public void setCreator(String user) {
        this.creator = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {return date;}

    public void setDate(LocalDate date) {this.date = date;}

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }
}
