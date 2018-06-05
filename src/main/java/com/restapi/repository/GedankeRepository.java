package com.restapi.repository;

import com.restapi.model.Gedanke;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GedankeRepository extends MongoRepository<Gedanke, String> {

    public Gedanke findGedankeById(String id);

    public List<Gedanke> findAllByAutor(String autor);

    public Long removeGedankeById(String id);


}
