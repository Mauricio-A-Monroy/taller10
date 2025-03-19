package edu.eci.arep.Microservice.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PostDTO {

    private String user;
    @Size(max = 140, message = "Content must be 140 characters or less")
    private String content;
    private String streamId;

    public PostDTO(){}

    public PostDTO(String user, String content, String streamId) {
        this.user = user;
        this.content = content;
        this.streamId = streamId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public @Size(max = 140, message = "Content must be 140 characters or less") String getContent() {
        return content;
    }

    public void setContent(@Size(max = 140, message = "Content must be 140 characters or less") String content) {
        this.content = content;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }
}