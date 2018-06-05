package com.restapi.converter;

import com.restapi.model.Consequence;
import org.bson.Document;

public class ConsequenceDTOConverter {

    public static Consequence docToCons(Document doc) {

        String username = (String) doc.get("autor");
        String situation = (String) doc.get("situation");
        String konsequenz = (String) doc.get("konsequenz");
        String reaktion = (String) doc.get("reaktion");
        String id = (String) doc.get("id");

        Consequence cons = new Consequence();

        cons.setAutor(username);
        cons.setReaktion(reaktion);
        cons.setKonsequenz(konsequenz);
        cons.setSituation(situation);
        cons.setId(id);

        return cons;


    }


}
