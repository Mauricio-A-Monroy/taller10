package edu.eci.arep.Microservice.dto;

import jakarta.validation.constraints.Size;

public class PostDTO {

    private String creator;
    @Size(max = 140, message = "Content must be 140 characters or less")
    private String content;
    private String streamId;

    public PostDTO(){}

    public PostDTO(String creator, String content, String streamId) {
        this.creator = creator;
        this.content = content;
        this.streamId = streamId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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