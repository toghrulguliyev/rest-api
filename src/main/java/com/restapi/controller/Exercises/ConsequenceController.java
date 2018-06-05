package com.restapi.controller.Exercises;

import com.restapi.converter.ConsequenceDTOConverter;
import com.restapi.model.Consequence;
import com.restapi.model.Response;
import com.restapi.repository.ConsequenceRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ConsequenceController {

    @Autowired
    ConsequenceRepository consequenceRepository;

    @PostMapping("get_cons")
    public ResponseEntity<List<Consequence>> getMyConsequences(@RequestParam("username") String username) {

        List<Consequence> cons = new ArrayList<Consequence>();

        cons = consequenceRepository.findAllByAutor(username);

        if (cons == null || cons.isEmpty()) {
            return new ResponseEntity<List<Consequence>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<Consequence>>(cons, HttpStatus.OK);
        }
    }

    @PostMapping("save_cons")
    public ResponseEntity<Response> saveCons(@RequestBody Document doc) {
        Consequence cons = new Consequence();

        cons = ConsequenceDTOConverter.docToCons(doc);
        Consequence oldCons = consequenceRepository.findConsequenceById(cons.getId());

        if (oldCons == null) {
            consequenceRepository.save(cons);
            Response response = new Response("Das neue Objekt wurde erfolgreich erzeugt", 201);
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        } else {
            oldCons.setSituation(cons.getSituation());
            oldCons.setReaktion(cons.getReaktion());
            oldCons.setKonsequenz(cons.getKonsequenz());
            consequenceRepository.save(oldCons);
            Response response = new Response("Objekt wurde erfolgreich aktualisiert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @PostMapping("remove_cons")
    public ResponseEntity<Response> removeCons(@RequestParam("id") String id) {

        if (consequenceRepository.findConsequenceById(id) == null) {
            Response response = new Response("Das Objekt wurde nicht gefunden", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            consequenceRepository.removeConsequenceById(id);
            Response response = new Response("Das Objekt wurde erfolgreich gelöscht", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }





}
