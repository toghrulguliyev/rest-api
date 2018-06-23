package com.restapi.controller;

import com.restapi.converter.EventDTOConverter;
import com.restapi.model.Event;
import com.restapi.model.Response;
import com.restapi.model.User;
import com.restapi.repository.EventRepository;
import com.restapi.repository.UserRepository;
import org.bson.Document;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebController webController;


    @RequestMapping("get_events")
    public ResponseEntity<List<Event>> getEvents(@RequestParam("username") String username, @RequestParam("familyId") String familyId) {

        List<Event> sumEvents = new ArrayList<Event>();
        List<Event> familyEvents = new ArrayList<Event>();
        List<Event> privateEvents = new ArrayList<Event>();
        privateEvents = eventRepository.findAllByAutor(username);

        if (familyId != null || !familyId.isEmpty()) {
            familyEvents = eventRepository.findAllByFamilyId(familyId);
        }

        if ((privateEvents == null || privateEvents.isEmpty()) && (familyEvents == null || familyEvents.isEmpty())) {
            return new ResponseEntity<List<Event>>(HttpStatus.NO_CONTENT);
        } else if (privateEvents.size() > 0 && (familyEvents == null || familyEvents.isEmpty())) {
            return new ResponseEntity<>(privateEvents, HttpStatus.OK);
        } else if ((privateEvents == null || privateEvents.isEmpty()) && familyEvents.size() > 0) {
            return new ResponseEntity<List<Event>>(familyEvents, HttpStatus.OK);
        } else if (privateEvents.size() > 0 && familyEvents.size() > 0) {
            for (Event event : privateEvents) {
                sumEvents.add(event);
            }
            for (Event event : familyEvents) {
                if (!username.equals(event.getAutor())) {
                    sumEvents.add(event);
                }
            }
            return new ResponseEntity<List<Event>>(sumEvents, HttpStatus.OK);
        }
        return new ResponseEntity<List<Event>>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("save_event")
    public ResponseEntity<Response> saveEvent(@RequestBody Document doc) {

        Event event = new Event();
        event = EventDTOConverter.documentToEvent(doc);

        Event checkEvent = (Event) eventRepository.findById(event.getId());

        if (checkEvent == null) {
            eventRepository.save(event);
            sendNotification(event);
            Response response = new Response("Termin wurde erfolgreich erzeugt", 201);
            return new ResponseEntity<Response>(response, HttpStatus.CREATED);
        } else {
            checkEvent = event;
            eventRepository.save(checkEvent);
            sendNotification(checkEvent);
            Response response = new Response("Termin wurde aktualisiert", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @RequestMapping("remove_event")
    public ResponseEntity<Response> removeEvent(@RequestParam("id") long id) {

        Event event = new Event();
        event = eventRepository.findById(id);

        if (event == null) {
            Response response = new Response("Termin wurde nicht gefunden", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            long remove = eventRepository.removeById(id);
            if (remove == 1) {
                Response response = new Response("Termin wurde erfolgreich gelöscht", 200);
                return new ResponseEntity<Response>(response, HttpStatus.OK);
            } else {
                Response response = new Response("Termin wurde nicht gelöscht", 400);
                return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
            }
        }
    }


    public void sendNotification(Event event) throws JSONException {
        List<User> users1 = new ArrayList<User>();
        User user1 = userRepository.findByUsername(event.getAutor());
        List<String> members = user1.getParentUsernames();
        if (members == null || members.isEmpty()) {
            System.out.println("No members available to notify");
        } else if (members != null && !members.isEmpty()) {
            for (String username : members) {
                User saveUser = userRepository.findByUsername(username);
                if (saveUser != null) {
                    users1.add(saveUser);
                }
            }

            if (users1 == null || users1.isEmpty()) {
                System.out.println("No family users found");
                return;
            } else if (users1 != null && !users1.isEmpty()) {
                if (event.getEventType().equals("Arbeit") || event.getEventType().equals("Familie")) {
                    for (User user : users1) {
                        if (!user.getUsername().equals(event.getAutor())) {
                            if (user.getFcmToken() != null && !user.getFcmToken().isEmpty())
                                webController.newFamilyEventNotification("Familientermin erzeugt", event.getMessage(), user.getFcmToken());
                        }
                    }
                } else if (event.getEventType().equals("Elternübung")) {
                    for (User user : users1) {
                        if (!user.getUsername().equals(event.getAutor()) && (user.getAccType() == 2 || user.getAccType() == 3)) {
                            if (user.getFcmToken() != null && !user.getParentUsernames().isEmpty()) {
                                webController.newFamilyEventNotification("Elternübungstermin wurde erzeugt", event.getMessage(), user.getFcmToken());
                            }
                        }
                    }
                } else if (event.getEventType().equals("Kindübung")) {
                    for (User user : users1) {
                        if (!user.getUsername().equals(event.getAutor()) && user.getAccType() == 1) {
                            if (user.getFcmToken() != null && !user.getParentUsernames().isEmpty()) {
                                webController.newFamilyEventNotification("Kindübungstermin wurde erzeugt", event.getMessage(), user.getFcmToken());
                            }
                        }
                    }
                } else if (event.getEventType().equals("Zeit zu Zweit") || event.getEventType().equals("Kind")) {
                    if (event.getNotifyUser() != null && !event.getNotifyUser().isEmpty() && !event.getNotifyUser().equals("---")) {
                        for (User user : users1) {
                            if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                                if (event.getNotifyUser().equals(user.getUsername())) {
                                    webController.newFamilyEventNotification("Termin Zur Zweit erzeugt" , event.getMessage(), user.getFcmToken());
                                }
                            }
                        }
                    }
                }
            }

        }

    }


}
