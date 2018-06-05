package com.restapi.repository;

import com.restapi.model.Family;
import com.restapi.model.FamilyRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRuleRepository extends MongoRepository<FamilyRule, String> {

    public FamilyRule getFamilyRuleById(String id);

    public List<FamilyRule> findByAutor(String autor);

    public List<FamilyRule> findByFamilyIdAndAutor(String familyId, String autor);

    public Long removeFamilyRuleById(String id);

    public List<FamilyRule> getAllByFamilyId(String familyId);

}
