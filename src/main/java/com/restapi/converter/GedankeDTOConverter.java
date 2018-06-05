package com.restapi.converter;

import com.restapi.model.Gedanke;
import org.bson.Document;

public class GedankeDTOConverter {

    public static Gedanke docToGedanke(Document doc) {

        String username = (String) doc.get("autor");
        String situation = (String) doc.get("situation");
        String bewertung = (String) doc.get("bewertung");
        String feel = (String) doc.get("feel");
        String altBewertung = (String) doc.get("altBewertung");
        String altReaktion = (String) doc.get("altReaktion");
        String id = (String) doc.get("id");

        Gedanke gedanke = new Gedanke();

        gedanke.setAutor(username);
        gedanke.setAltBewertung(altBewertung);
        gedanke.setAltReaktion(altReaktion);
        gedanke.setBewertung(bewertung);
        gedanke.setFeel(feel);
        gedanke.setSituation(situation);
        gedanke.setId(id);

        return gedanke;


    }

}
