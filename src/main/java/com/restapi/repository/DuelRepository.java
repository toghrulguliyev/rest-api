package com.restapi.repository;

import com.restapi.model.quizDuel.Duel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DuelRepository extends MongoRepository<Duel, String> {

    public List<Duel> findAllByAutor(String autor);

    public List<Duel> findAllByOpponent(String opponent);

    public Duel getById(String id);

}
