package com.restapi.repository;

import com.restapi.model.Emotions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmotionsRepository extends MongoRepository<Emotions, String> {

    public Emotions findByAutor(String autor);

    public List<Emotions> findAllByFamilyId(String familyId);

    public List<Emotions> findAllByAutor(List<String> autor);
}
