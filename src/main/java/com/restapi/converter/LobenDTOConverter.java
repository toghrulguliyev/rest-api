package com.restapi.converter;

import com.restapi.model.Loben;
import org.bson.Document;

public class LobenDTOConverter {

    public static Loben docToLoben(Document doc) {

        String username = (String) doc.get("autor");
        String situation = (String) doc.get("situation");
        String art = (String) doc.get("art");
        String reaktion = (String) doc.get("reaktion");
        String id = (String) doc.get("id");

        Loben loben = new Loben();

        loben.setAutor(username);
        loben.setReaktion(reaktion);
        loben.setArt(art);
        loben.setSituation(situation);
        loben.setId(id);

        return loben;
    }

}
