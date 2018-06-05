package com.restapi.repository;

import com.restapi.model.MyGoals;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyGoalsRepository extends MongoRepository<MyGoals, String> {

    public MyGoals findByAutor(String autor);

    public MyGoals findById(ObjectId id);

}
