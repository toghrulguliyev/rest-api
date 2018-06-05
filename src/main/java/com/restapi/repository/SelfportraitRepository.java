package com.restapi.repository;

import com.restapi.model.Selfportrait;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfportraitRepository extends MongoRepository<Selfportrait, String> {

    public Selfportrait findByAutor(String autor);

    public Long deleteByAutor(String autor);

}
