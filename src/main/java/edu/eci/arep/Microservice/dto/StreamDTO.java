package edu.eci.arep.Microservice.dto;

import java.time.LocalDate;
import java.util.List;
import edu.eci.arep.Microservice.model.Post;

public class StreamDTO {

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