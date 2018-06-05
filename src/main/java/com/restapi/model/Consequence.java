package com.restapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "consequences")
public class Consequence {

    @Id
    private String id;
    private String situation, reaktion, konsequenz;
    private String autor;

    public Consequence() {}

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getReaktion() {
        return reaktion;
    }

    public void setReaktion(String reaktion) {
        this.reaktion = reaktion;
    }

    public String getKonsequenz() {
        return konsequenz;
    }

    public void setKonsequenz(String konsequenz) {
        this.konsequenz = konsequenz;
    }
}