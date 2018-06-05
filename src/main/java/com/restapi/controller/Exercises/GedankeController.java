package com.restapi.controller.Exercises;

import com.restapi.converter.GedankeDTOConverter;
import com.restapi.model.Gedanke;
import com.restapi.model.Response;
import com.restapi.repository.GedankeRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GedankeController {

    @Autowired
    GedankeRepository gedankeRepository;

    @PostMapping("get_my_gedanke")
    public ResponseEntity<List<Gedanke>> getMyGedanke(@RequestParam("username") String username) {

        List<Gedanke> gedanken = new ArrayList<Gedanke>();

        gedanken = gedankeRepository.findAllByAutor(username);

        if (gedanken == null || gedanken.isEmpty()) {
            return new ResponseEntity<List<Gedanke>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<Gedanke>>(gedanken, HttpStatus.OK);
        }
    }

    @PostMapping("save_gedanke")
    public ResponseEntity<Response> saveGedanke(@RequestBody Document doc) {

        Gedanke gedanke = new Gedanke();

        gedanke = GedankeDTOConverter.docToGedanke(doc);
        Gedanke oldGedanke = gedankeRepository.findGedankeById(gedanke.getId());

        System.out.println(gedanke.getAutor());
        System.out.println(gedanke.getSituation());
        System.out.println(gedanke.getBewertung());
        System.out.println(gedanke.getAltBewertung());
        System.out.println(gedanke.getAltReaktion());
        System.out.println(gedanke.getFeel());

        if (oldGedanke == null) {
            gedankeRepository.save(gedanke);
            Response response = new Response("Das neue Objekt wurde erfolgreich erzeugt", 201);
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        } else {
            oldGedanke.setSituation(gedanke.getSituation());
            oldGedanke.setFeel(gedanke.getFeel());
            oldGedanke.setAltReaktion(gedanke.getAltReaktion());
            oldGedanke.setAltBewertung(gedanke.getAltBewertung());
            oldGedanke.setBewertung(gedanke.getBewertung());
            gedankeRepository.save(oldGedanke);
            Response response = new Response("Objekt wurde erfolgreich aktualisiert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @PostMapping("remove_gedanke")
    public ResponseEntity<Response> removeGedanke(@RequestParam("username") String username, @RequestParam("id") String id) {

        if (gedankeRepository.findGedankeById(id) == null) {
            Response response = new Response("Das Objekt wurde nicht gefunden", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            gedankeRepository.removeGedankeById(id);
            Response response = new Response("Das Objekt wurde erfolgreich gelöscht", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }


}
