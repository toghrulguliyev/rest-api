package com.restapi.converter;

import com.restapi.model.Selfportrait;
import org.bson.Document;

import java.util.List;

public class SelfportraitDTOConverter {

    public static Selfportrait docToSp(Document doc) {
        String username = (String) doc.get("autor");

        Selfportrait sp = new Selfportrait(username);

        if (doc.get("strengths") != null && !doc.get("strengths").toString().isEmpty()) {
            List<String> strengths = (List<String>) doc.get("strengths");
            sp.setStrengths(strengths);
        }
        if (doc.get("weaknesses") != null && !doc.get("weaknesses").toString().isEmpty()) {
            List<String> weaknesses = (List<String>) doc.get("weaknesses");
            sp.setWeaknesses(weaknesses);
        }
        return sp;
    }

}
