package com.restapi.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "emotions")
public class Emotions {

    @Id
    private String autor;
    private float[] emotions;
    private String familyId;

    public Emotions(String autor, float[] emotions) {
        this.autor = autor;
        this.emotions = emotions;
    }

    public Emotions(){}

    public Emotions(String autor) {
        this.autor = autor;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public float[] getEmotions() {
        return emotions;
    }

    public void setEmotions(float[] emotions) {
        this.emotions = emotions;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
}
