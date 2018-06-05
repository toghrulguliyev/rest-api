package com.restapi.controller.Exercises;


import com.restapi.converter.SelfportraitDTOConverter;
import com.restapi.model.Response;
import com.restapi.model.Selfportrait;
import com.restapi.repository.SelfportraitRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SelfportraitController {

    @Autowired
    SelfportraitRepository spRepository;

    @PostMapping("get_quality")
    public ResponseEntity<Selfportrait> getQuality(@RequestParam("username") String username) {

        Selfportrait sp = spRepository.findByAutor(username);

        if (sp == null) {
            return new ResponseEntity<Selfportrait>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Selfportrait>(sp, HttpStatus.OK);
        }
    }

    @PostMapping("save_qualities")
    public ResponseEntity<Response> saveQualities(@RequestBody Document doc) {

        Selfportrait sp = SelfportraitDTOConverter.docToSp(doc);

        System.out.println(sp.getAutor());

        if (spRepository.findByAutor(sp.getAutor()) == null) {
            spRepository.save(sp);
            Response response = new Response("Selbstbild erzeugt", 201);
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        } else {
            spRepository.save(sp);
            Response response = new Response("Selbstbild aktualisiert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @PostMapping("remove_quality")
    public ResponseEntity<Response> removeQuality(@RequestBody Document doc) {


        Selfportrait sp = SelfportraitDTOConverter.docToSp(doc);

        if (sp == null) {
            Response response = new Response("Keine Daten gefunden", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            spRepository.deleteByAutor(sp.getAutor());
            Response response = new Response("Erfolgreich gelöscht", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }
}
