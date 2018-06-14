package com.restapi.model.quizDuel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "score")
public class Score {

    @Id
    private String id;
    private String autor, opponent;
    private String category;
    private int scoreAutor, scoreOpponent;


    public Score(String id, String autor, String opponent, String category) {
        this.id = id;
        this.autor = autor;
        this.opponent = opponent;
        this.category = category;
    }

    public void plusAutorScore(int score) {
        this.scoreAutor = this.scoreAutor + score;
    }

    public void plusOpponentScore(int score) {
        this.scoreOpponent = this.scoreOpponent + score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScoreAutor() {
        return scoreAutor;
    }

    public void setScoreAutor(int scoreAutor) {
        this.scoreAutor = scoreAutor;
    }

    public int getScoreOpponent() {
        return scoreOpponent;
    }

    public void setScoreOpponent(int scoreOpponent) {
        this.scoreOpponent = scoreOpponent;
    }

}
