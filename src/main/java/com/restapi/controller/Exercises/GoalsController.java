package com.restapi.controller.Exercises;

import com.restapi.converter.MyGoalsDTOConverter;
import com.restapi.model.Goals;
import com.restapi.model.MyGoals;
import com.restapi.model.Response;
import com.restapi.repository.GoalsRepository;
import com.restapi.repository.MyGoalsRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class GoalsController {

    @Autowired
    private GoalsRepository goalsRepository;
    @Autowired
    private MyGoalsRepository myGoalsRepository;

    @PostMapping("get_my_goals")
    public ResponseEntity<MyGoals> getMyGoals(@RequestParam("username") String username) {

        MyGoals myGoals = myGoalsRepository.findByAutor(username);

        if (myGoals == null) {
            return new ResponseEntity<MyGoals>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<MyGoals>(myGoals, HttpStatus.OK);
        }
    }

    @PostMapping("save_my_goals")
    public ResponseEntity<Response> saveMyGoals(@RequestBody Document doc) {

        MyGoals myGoals = MyGoalsDTOConverter.DocToMG(doc);
        MyGoals oldGoals = myGoalsRepository.findByAutor(myGoals.getAutor());

        if (oldGoals == null) {
            myGoalsRepository.save(myGoals);
            Response response = new Response("Meine Zielenreihenfolge wurde  gespeichert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } else {
            oldGoals.setGoals(myGoals.getGoals());
            myGoalsRepository.save(oldGoals);
            Response response = new Response("Meine Zielenreihenfolge wurde erfolgreich aktualisiert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @PostMapping("get_all_goals")
    public ResponseEntity<Goals> getAllGoals() {
            Goals goals = goalsRepository.findById(new ObjectId("5b0eb407db39bc4ccdaa5dad"));
            if (goals == null) {
                return new ResponseEntity<Goals>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Goals>(goals, HttpStatus.OK);
            }
    }

    @PostMapping("save_goals")
    public ResponseEntity<Response> saveGoals(@RequestParam("real") ArrayList<String> real, @RequestParam("unreal") ArrayList<String> unreal) {
        //List<String> realGoals = new ArrayList<String>();
        //List<String> unrealGoals = new ArrayList<String>();

        Goals goals = new Goals(new ArrayList<String>(), new ArrayList<String>());

        ObjectId id = new ObjectId("5b0eb407db39bc4ccdaa5dad");

        goals = goalsRepository.findById(id);

        if (goals == null) {
            Response response = new Response("Goals nicht gefunden", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            if (real != null & unreal != null && !real.isEmpty() && !unreal.isEmpty()) {
                System.out.println("Proshel");
                Response response = new Response("Neue Ziele wurden gespeichert", 200);
                return new ResponseEntity<Response>(response, HttpStatus.OK);
            }
        }
        Response response = new Response(id.toString(), 400);
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }
}
