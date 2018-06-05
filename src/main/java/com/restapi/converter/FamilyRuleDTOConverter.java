package com.restapi.converter;

import com.restapi.model.FamilyRule;
import org.bson.Document;

public class FamilyRuleDTOConverter {

    public static Document familyRuletoDocument(FamilyRule fr) {
        Document doc = new Document();

        doc.append("autor", fr.getAutor());
        doc.append("ruleName", fr.getRuleName());
        doc.append("rule", fr.getRule());
        doc.append("reason", fr.getReason());
        doc.append("forWho", fr.getForWho());
        doc.append("id", fr.getId());

        return doc;
    }

    public static FamilyRule documentToFamilyRule(Document doc) {

        String id = (String) doc.get("id");
        String familyId = "";
        String ruleName = (String) doc.get("ruleName");
        String rule = (String) doc.get("rule");
        String reason = (String) doc.get("reason");
        String forWho = (String) doc.get("forWho");
        String autor = (String) doc.get("autor");
        if (doc.get("familyId").toString() != null && !doc.get("familyId").toString().isEmpty()) {
            familyId = (String) doc.get("familyId");
        }
        if (familyId == null || familyId.isEmpty()) {
            FamilyRule fr = new FamilyRule(autor, ruleName, rule, reason, forWho, id);
            return fr;
        } else {
            FamilyRule fr = new FamilyRule(autor, ruleName, rule, reason, forWho, id);
            fr.setFamilyId(familyId);
            return fr;
        }
    }
}
