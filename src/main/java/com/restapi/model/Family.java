package com.restapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "family")
public class Family {

    @Id
    private String familyId;
    private String familyName;
    private List<User> familyList;



}
