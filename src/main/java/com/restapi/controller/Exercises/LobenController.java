package com.restapi.controller.Exercises;

import com.restapi.converter.LobenDTOConverter;
import com.restapi.model.Loben;
import com.restapi.model.Response;
import com.restapi.repository.LobenRepository;
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
public class LobenController {

    @Autowired
    LobenRepository lobenRepository;

    @PostMapping("get_loben")
    public ResponseEntity<List<Loben>> getMyConsequences(@RequestParam("username") String username) {

        List<Loben> loben = new ArrayList<Loben>();

        loben = lobenRepository.findAllByAutor(username);

        if (loben == null || loben.isEmpty()) {
            return new ResponseEntity<List<Loben>>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<Loben>>(loben, HttpStatus.OK);
        }
    }

    @PostMapping("save_loben")
    public ResponseEntity<Response> saveCons(@RequestBody Document doc) {
        Loben loben = new Loben();

        loben = LobenDTOConverter.docToLoben(doc);
        Loben oldLoben = lobenRepository.findLobenById(loben.getId());

        if (oldLoben == null) {
            lobenRepository.save(loben);
            Response response = new Response("Das neue Objekt wurde erfolgreich erzeugt", 201);
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        } else {
            oldLoben.setSituation(loben.getSituation());
            oldLoben.setReaktion(loben.getReaktion());
            oldLoben.setArt(loben.getArt());
            lobenRepository.save(oldLoben);
            Response response = new Response("Objekt wurde erfolgreich aktualisiert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @PostMapping("remove_loben")
    public ResponseEntity<Response> removeCons(@RequestParam("id") String id) {

        if (lobenRepository.findLobenById(id) == null) {
            Response response = new Response("Das Objekt wurde nicht gefunden", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            lobenRepository.removeLobenById(id);
            Response response = new Response("Das Objekt wurde erfolgreich gelöscht", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }




}
