package com.restapi.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.restapi.service.PushNotificationService.AndroidPushNotificationsService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebController {

    private final String TOPIC = "nachrichten";

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService = new AndroidPushNotificationsService();

    @Scheduled(cron = "0 15 11 ? * SAT")
    @RequestMapping(value = "/nachrichten", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> send() throws JSONException {

        JSONObject body = new JSONObject();
        body.put("to", "/topics/" + TOPIC);
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", "Emotionstest Erinnerung");
        notification.put("body", "Sie müssen die Übung wiederholen!");

        JSONObject data = new JSONObject();
        data.put("Key-1", "Data 1");
        data.put("Key-2", "Data 2");

        body.put("notification", notification);
        body.put("data", data);

/**
 {
 "notification": {
 "title": "JSA Notification",
 "body": "Happy Message!"
 },
 "data": {
 "Key-1": "JSA Data 1",
 "Key-2": "JSA Data 2"
 },
 "to": "/topics/JavaSampleApproach",
 "priority": "high"
 }
 */

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<String>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>("Push Benachrichtigungsfehler", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/new_event", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> newFamilyEventNotification(String title, String saveBody, String fcmToken) throws JSONException {

        JSONObject body = new JSONObject();
        body.put("to", fcmToken);
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", title);
        notification.put("body", saveBody);

        JSONObject data = new JSONObject();
        data.put("Key-1", "Data 1");
        data.put("Key-2", "Data 2");

        body.put("notification", notification);
        body.put("data", data);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<String>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>("Push Benachrichtigungsfehler", HttpStatus.BAD_REQUEST);
    }

}