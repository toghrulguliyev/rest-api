package com.restapi.controller.Exercises;

import com.restapi.model.Emotions;
import com.restapi.model.Response;
import com.restapi.model.User;
import com.restapi.repository.EmotionsRepository;
import com.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EmotionsController {

    @Autowired
    EmotionsRepository emoRepository;
    @Autowired
    UserRepository userRepository;

    @PostMapping("get_emotions")
    public ResponseEntity<Emotions> getEmotions(@RequestParam("username") String username) {

        Emotions emo = emoRepository.findByAutor(username);

        System.out.println(emo.getAutor());

        if (emo == null) {
            return new ResponseEntity<Emotions>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<Emotions>(emo, HttpStatus.OK);
        }
    }

    @PostMapping("get_family_emotions")
    public ResponseEntity<List<Emotions>> getFamilyEmotions(@RequestParam("username") String username) {
        List<Emotions> emoList = new ArrayList<>();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<List<Emotions>>(HttpStatus.NOT_FOUND);
        } else {
            if (user.getParentUsernames() == null || user.getParentUsernames().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                for (int i = 0; i < user.getParentUsernames().size(); i++) {
                    emoList.add(emoRepository.findByAutor(user.getParentUsernames().get(i)));
                }
                return new ResponseEntity<List<Emotions>>(emoList, HttpStatus.OK);
            }
        }
    }

    @PostMapping("save_emotions")
    public ResponseEntity<Response> saveEmotions(@RequestParam("username") String username, @RequestParam("emotions") float[] emotions) {

        Emotions emo = emoRepository.findByAutor(username);

        if (emo == null) {
            emoRepository.save(new Emotions(username, emotions));
            Response response = new Response("Neuer IST Zustand wurde erzeugt", 201);
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        } else {
            emo.setEmotions(emotions);
            emoRepository.save(emo);
            Response response = new Response("IST Zustand wurde aktualisiert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

}
