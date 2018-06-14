package com.restapi.model.quizDuel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Document(collection = "duel")
public class Duel {

    @Id
    private String id;
    private String autor;
    private String opponent;
    private List<Question> questions = new ArrayList<Question>();
    private String status;
    private String category;
    private Score score;
    private boolean autorStatus, opponentStatus;
    private String winner;

    public Duel(String autor, String opponent, List<Question> questions, String status, String category) {
        this.autor = autor;
        this.opponent = opponent;
        this.questions = questions;
        this.status = status;
        this.category = category;
        this.id = UUID.randomUUID().toString();
        this.score = new Score(this.id, this.autor, this.opponent, this.category);
        this.autorStatus = false;
        this.opponentStatus = false;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public boolean isAutorStatus() {
        return autorStatus;
    }

    public void setAutorStatus(boolean autorStatus) {
        this.autorStatus = autorStatus;
    }

    public boolean isOpponentStatus() {
        return opponentStatus;
    }

    public void setOpponentStatus(boolean opponentStatus) {
        this.opponentStatus = opponentStatus;
    }

    public List<Question> getShuffeledQuestions() {
        Collections.shuffle(questions);
        return questions;
    }

    public void setAutorScore(int score) {
        this.score.setScoreAutor(score);
    }

    public void setOpponentScore(int score) {
        this.score.setScoreOpponent(score);
    }

    public Score getScore() {
        return this.score;
    }

    public int getAutorScore() {
        return this.score.getScoreAutor();
    }

    public int getOpponentScore() {
        return this.score.getScoreOpponent();
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
