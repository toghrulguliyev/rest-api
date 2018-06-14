package com.restapi.repository;

import com.restapi.model.quizDuel.Score;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScoreRepository extends MongoRepository<Score, String> {

    public List<Score> findAllByAutor(String autor);

    public List<Score> findAllByOpponent(String opponent);

}
