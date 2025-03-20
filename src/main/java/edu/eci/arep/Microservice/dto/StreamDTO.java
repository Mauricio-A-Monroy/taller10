package edu.eci.arep.Microservice.dto;

import jakarta.validation.constraints.NotBlank;

public class StreamDTO {

    @NotBlank(message = "Creator is required")
    private String creator;

    public StreamDTO(){}

    public StreamDTO(String creator){
        this.creator = creator;
    }

    public String getCreator(){
        return creator;
    }

    public void setCreator(String creator){
        this.creator = creator;
    }

}