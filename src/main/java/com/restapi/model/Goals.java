package com.restapi.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "goals")
public class Goals {

    @Id
    private ObjectId id;

    private List<String> realGoals = new ArrayList<String>();
    private List<String> unrealGoals = new ArrayList<String>();

    public Goals(List<String> realGoals, List<String> unrealGoals) {
        this.realGoals = new ArrayList<String>();
        this.unrealGoals = new ArrayList<String>();
        this.realGoals = realGoals;
        this.unrealGoals = unrealGoals;
    }

    public List<String> getRealGoals() {
        return realGoals;
    }

    public void setRealGoals(List<String> goals) {
        this.realGoals = goals;
    }

    public void addRealGoal(String realGoal) {
        this.realGoals.add(realGoal);
    }

    public void addUnrealGoal(String unrealGoal) {
        this.unrealGoals.add(unrealGoal);
    }

    public List<String> getUnrealGoals() {
        return unrealGoals;
    }

    public void setUnrealGoals(List<String> unrealGoals) {
        this.unrealGoals = unrealGoals;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
