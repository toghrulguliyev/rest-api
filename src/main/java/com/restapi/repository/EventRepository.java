package com.restapi.repository;

import com.restapi.model.Event;
import com.restapi.model.Response;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    public List<Event> findAllByAutor(String autor);

    public List<Event> findAllByFamilyId(String familyId);

    public Long removeById(long id);

    public Event findById(long id);


}
