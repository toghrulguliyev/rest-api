package com.restapi.repository;

import com.restapi.model.quizDuel.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, String> {

    public List<Question> findAllByCategory(String category);

    public List<Question> getByCategory(String category);
}
