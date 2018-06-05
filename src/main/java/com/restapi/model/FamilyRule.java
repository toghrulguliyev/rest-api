package com.restapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "familyrules")
public class FamilyRule {

    private String ruleName;
    private String rule;
    private String reason;
    private String forWho;
    @Id
    private String id;
    private String autor;
    private String familyId = "";

    public FamilyRule(String autor, String ruleName, String rule, String reason, String forWho, String id) {
        this.ruleName = ruleName;
        this.reason = reason;
        this.forWho = forWho;
        this.id = id; //UUID.randomUUID().toString();
        this.rule = rule;
        this.autor = autor;
    }

    public void updateRule(String autor, String ruleName, String rule, String reason, String forWho, String familyId) {
        this.ruleName = ruleName;
        this.reason = reason;
        this.forWho = forWho;
        this.rule = rule;
        this.autor = autor;
        this.familyId = familyId;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getForWho() {
        return this.forWho;
    }

    public void setForWho(String forWho) {
        this.forWho = forWho;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
}
