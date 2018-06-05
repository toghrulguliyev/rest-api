package com.restapi.repository;

import com.restapi.model.Loben;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface LobenRepository extends MongoRepository<Loben, String> {

    public Loben findLobenById(String id);

    public List<Loben> findAllByAutor(String autor);

    public Long removeLobenById(String id);

}
