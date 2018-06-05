package com.restapi.repository;

import com.restapi.model.Consequence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsequenceRepository extends MongoRepository<Consequence, String> {

    public Consequence findConsequenceById(String id);

    public List<Consequence> findAllByAutor(String autor);

    public Long removeConsequenceById(String id);


}
