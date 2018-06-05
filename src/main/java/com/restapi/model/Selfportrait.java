package com.restapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "selfportraits")
public class Selfportrait {

    private List<String> strengths;
    private List<String> weaknesses;

    @Id
    private String autor;

    public Selfportrait(String autor) {
        this.autor = autor;
        strengths = new ArrayList<String>();
        weaknesses = new ArrayList<String>();
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void addStrength(String strength) {
        this.strengths.add(strength);
    }

    public void addWeakness(String weakness) {
        this.weaknesses.add(weakness);
    }
}
