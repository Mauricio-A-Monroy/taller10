package edu.eci.arep.Microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "streams")
public class Stream {

    @Id
    private String id;
    private String creator;
    private LocalDate date;

    public Stream() {
        this.date = LocalDate.now();
    }

    public Stream(String creator) {
        this.creator = creator;
        this.date = LocalDate.now();
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

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
}