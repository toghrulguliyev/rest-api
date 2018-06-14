package com.restapi.controller;


import com.restapi.converter.FamilyRuleDTOConverter;
import com.restapi.exception.CustomException;
import com.restapi.exception.ErrorResponse;
import com.restapi.model.FamilyRule;
import com.restapi.model.Response;
import com.restapi.model.User;
import com.restapi.repository.FamilyRuleRepository;
import com.restapi.repository.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FamilyRulesController {

    @Autowired
    private FamilyRuleRepository ruleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebController webController;

    @PostMapping("/save_rule")
    public ResponseEntity<Response> saverule(@RequestBody Document doc) {

        FamilyRule fr = FamilyRuleDTOConverter.documentToFamilyRule(doc);
        FamilyRule oldFr = ruleRepository.getFamilyRuleById(fr.getId());

        List<User> userList = new ArrayList<User>();
        userList = userRepository.findByFamilyId(fr.getFamilyId());


        if (oldFr == null) {
            ruleRepository.save(fr);
            if (userList != null && !userList.isEmpty()) {
                for (User user : userList) {
                    if (!user.getUsername().equals(fr.getAutor())) {
                        if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                            webController.newFamilyEventNotification(fr.getRuleName(), "Familienregel wurde erzeugt", user.getFcmToken());
                        }
                    }
                }
            }
            Response response = new Response("Familienregel erzeugt", 201);
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        } else if (fr.getId().equals(oldFr.getId())) {
            oldFr.updateRule(fr.getAutor(), fr.getRuleName(), fr.getRule(), fr.getReason(), fr.getForWho(), fr.getFamilyId());
            ruleRepository.save(oldFr);
            if (userList != null && !userList.isEmpty()) {
                for (User user : userList) {
                    if (!user.getUsername().equals(oldFr.getAutor())) {
                        if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                            webController.newFamilyEventNotification(oldFr.getRuleName(), "Familienregel wurde aktualisiert", user.getFcmToken());
                        }
                    }
                }
            }
            Response response = new Response("Die Regel wurde aktualisiert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } else {
            Response response = new Response("Die Regel wurde NICHT aktualisiert.", 400);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/get_rules")
    public ResponseEntity<List<FamilyRule>> getrules(@RequestParam("username") String autor, @RequestParam("familyId") String familyId) {
        if (familyId == null || familyId.isEmpty()) {
            if (!autor.isEmpty()) {
                List<FamilyRule> fr = ruleRepository.findByAutor(autor);
                if (fr.isEmpty()) {
                    return new ResponseEntity<List<FamilyRule>>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<List<FamilyRule>>(fr, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<List<FamilyRule>>(HttpStatus.BAD_REQUEST);
            }
        } else if (!familyId.isEmpty() && !autor.isEmpty()) {
            List<FamilyRule> familyRulesByFamilyId = ruleRepository.getAllByFamilyId(familyId);
            List<FamilyRule> familyRulesByAutor = ruleRepository.findByAutor(autor);
            List<FamilyRule> fr = new ArrayList<FamilyRule>();
            if (familyRulesByAutor != null && !familyRulesByAutor.isEmpty()) {
                for (FamilyRule familyRule : familyRulesByAutor) {
                    fr.add(familyRule);
                }
            }
            if (familyRulesByFamilyId != null && !familyRulesByFamilyId.isEmpty()) {
                for (FamilyRule frId : familyRulesByFamilyId) {
                    if (familyRulesByAutor != null && !familyRulesByAutor.isEmpty()) {
                        if (!autor.equals(frId.getAutor())) {
                            fr.add(frId);
                        }
                    }
                }
            }
            if (fr.isEmpty()) {
                return new ResponseEntity<List<FamilyRule>>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<List<FamilyRule>>(fr, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<List<FamilyRule>>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("remove_fr")
    public ResponseEntity<Response> removeFr(@RequestParam("id") String id) {

        FamilyRule fr = ruleRepository.getFamilyRuleById(id);

        if (fr == null) {
            Response response = new Response("Familienregel wurde nicht gefunden", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            ruleRepository.removeFamilyRuleById(id);
            Response response = new Response("Regel wurde erfolgreich gelöscht", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {

        ErrorResponse error = new ErrorResponse();

        error.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());

        error.setMessage(ex.getMessage());

        return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);

    }



}
