package com.restapi.repository;

import com.restapi.model.Goals;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalsRepository extends MongoRepository<Goals, String> {

    public Goals findById(ObjectId id);


}
