package com.restapi.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "myGoals")
public class MyGoals {

    private List<String> goals;
    private String autor;
    @Id
    private ObjectId id;


    public MyGoals(List<String> goals, String autor) {
        this.goals = new ArrayList<String>();
        this.goals = goals;
        this.autor = autor;
    }

    public List<String> getGoals() {
        return goals;
    }

    public void setGoals(List<String> goals) {
        this.goals = goals;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
