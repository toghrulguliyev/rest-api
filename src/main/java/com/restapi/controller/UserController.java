package com.restapi.controller;

import com.restapi.converter.UserDTOConverter;
import com.restapi.exception.CustomException;
import com.restapi.exception.ErrorResponse;
import com.restapi.model.Response;
import com.restapi.model.Role;
import com.restapi.model.User;
import com.restapi.repository.UserRepository;
import com.restapi.security.constants.SecurityConst;
import com.restapi.security.service.PasswordHandler;
import com.restapi.security.service.TokenHandler;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final TokenHandler tokenHandler = new TokenHandler();

    @Autowired
    WebController webController;


    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@RequestBody Document doc) throws CustomException {
        User user = UserDTOConverter.documentToUser(doc);
        user.encodePassword();
        if (userRepository.findByUsername(user.getUsername()) != null) {
            Response response = new Response("Username ist schon vorhanden", 409);
            return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
        }

        ObjectId id = userRepository.save(user).getId();
        String token = tokenHandler.generateToken(user, id);//tokenHandler.generateAccessToken(id, LocalDateTime.now().plusDays(14));
        Response response = new Response("Registration war erfolgreich", 201);
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConst.AUTH_HEADER_NAME, token);
        headers.add("username", user.getUsername());
        return new ResponseEntity<Response>(response, headers, HttpStatus.CREATED);
    }

    @PostMapping("/userlogin")
    public ResponseEntity<Response>  userlogin(@RequestBody Document doc) throws CustomException {


        User user = userRepository.findByUsername(doc.get("username").toString());
        if (user == null) {
            Response response = new Response("User wurde nicht gefunden.", 401);
            return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
        } else {
            if (PasswordHandler.checkPassword(doc.get("password").toString(), user.getPassword())) {

                String token = tokenHandler.generateToken(user, user.getId());
                Response response = new Response("Sie sind angemeldet.", 201);
                HttpHeaders headers = new HttpHeaders();
                headers.add(SecurityConst.AUTH_HEADER_NAME, token);
                headers.add("username", user.getUsername());
                if (user.getFamilyId() != null || !user.getFamilyId().isEmpty()) {
                    headers.add("familyId", user.getFamilyId());
                }
                return new ResponseEntity<Response>(response, headers, HttpStatus.CREATED);
            } else {
                Response response = new Response("Username oder password ist nicht korrekt.", 401);
                return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @RequestMapping("/createuser")
    public String createUser() {
        User user = new User("Max", "Mustermann", "max", "max@mustermann", "max", 19911010, "male", true, 1);
        //user.setAccountNonLocked(true);
        user.encodePassword();
        List<Role> roles = user.getAuthorities();
        roles.add(Role.ADMIN);
        user.setAuthorities(roles);
        ObjectId id = userRepository.save(user).getId();
        String token = tokenHandler.generateToken(user, id);
        return (token);
    }

    @RequestMapping("/deleteuser")
    public Long deleteuser(@RequestBody Document doc) throws CustomException {
        return userRepository.deleteUserByUsername(doc.getString("username"));
    }

    @RequestMapping("/change_pwd")
    public ResponseEntity<Response> changePwd(@RequestParam("username") String username, @RequestParam("newPassword") String newPassword) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            Response response = new Response("User wurde nicht gefunden.", 401);
            return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
        } else {
            user.setPassword(newPassword);
            user.encodePassword();
            userRepository.save(user);
            return new ResponseEntity<Response>(new Response("Passwort erfolgreich geändert", 200), HttpStatus.OK);
        }
    }

    @PostMapping("/get_user")
    public ResponseEntity<User> getUser(@RequestParam("username") String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("User == null");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        } else {
            System.out.println(user.getEmail());
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
    }

    @PostMapping("/add_relative")
    public ResponseEntity<Response> addRelative(@RequestParam("username") String username, @RequestParam("relative") String relative) {

        User user = userRepository.findByUsername(username);

        User rel = userRepository.findByUsername(relative);

        if (user == null || rel == null) {
            Response response = new Response("Username nicht gefunden", 400);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        if (user != null && rel != null) {
            if ((user.getFamilyId() == null || user.getFamilyId().isEmpty()) && (user.getParentUsernames() == null || user.getParentUsernames().isEmpty())) {
                if ((rel.getFamilyId() == null || rel.getFamilyId().isEmpty()) && (rel.getParentUsernames() == null || rel.getParentUsernames().isEmpty())) {
                        //if (rel.getParentUsernames() == null || rel.getParentUsernames().isEmpty()) {
                    String familyId = user.generateFamilyId();
                    rel.setFamilyId(familyId);
                    user.addConnectedUser(relative);
                    rel.addConnectedUser(username);
                    userRepository.save(user);
                    userRepository.save(rel);
                    if (rel.getFcmToken() != null && !rel.getFcmToken().isEmpty()) {
                        webController.newFamilyEventNotification("Du hast eine neue Familienverbindung", user.getUsername() + " hat dich als Mitglied hinzugefügt", rel.getFcmToken());
                    }
                    HttpHeaders header = new HttpHeaders();
                    header.add("familyId", familyId);
                    Response response = new Response(relative + " wurde hinzugefügt", 200);
                    return new ResponseEntity<Response>(response, header, HttpStatus.OK);
                       // }
                } else if ((rel.getFamilyId() != null && !rel.getFamilyId().isEmpty()) && (rel.getParentUsernames() != null && !rel.getParentUsernames().isEmpty())) {
                    if (!rel.getParentUsernames().contains(username)) {
                        user.setFamilyId(rel.getFamilyId());
                        user.addConnectedUser(relative);
                        rel.addConnectedUser(username);
                        userRepository.save(user);
                        userRepository.save(rel);
                        if (rel.getFcmToken() != null && !rel.getFcmToken().isEmpty()) {
                            webController.newFamilyEventNotification("Du hast eine neue Familienverbindung", user.getUsername() + " hat dich als Mitglied hinzugefügt", rel.getFcmToken());
                        }
                        HttpHeaders header = new HttpHeaders();
                        header.add("familyId", user.getFamilyId());
                        Response response = new Response(relative + " wurde hinzugefügt", 200);
                        return new ResponseEntity<Response>(response, header, HttpStatus.OK);
                    } else {
                        user.setFamilyId(rel.getFamilyId());
                        user.addConnectedUser(relative);
                            //rel.addConnectedUser(username);
                        userRepository.save(user);
                        userRepository.save(rel);
                        if (rel.getFcmToken() != null && !rel.getFcmToken().isEmpty()) {
                            webController.newFamilyEventNotification("Du hast eine neue Familienverbindung", user.getUsername() + " hat dich als Mitglied hinzugefügt", rel.getFcmToken());
                        }
                        HttpHeaders header = new HttpHeaders();
                        header.add("familyId", user.getFamilyId());
                        Response response = new Response(relative + " wurde hinzugefügt", 200);
                        return new ResponseEntity<Response>(response, header, HttpStatus.OK);
                    }
                }
            } else if ((user.getFamilyId() != null && !user.getFamilyId().isEmpty()) && (user.getParentUsernames() != null && !user.getParentUsernames().isEmpty())) {
               if ((rel.getFamilyId() == null || rel.getFamilyId().isEmpty()) && (rel.getParentUsernames() == null || rel.getParentUsernames().isEmpty())) {
                   if (!user.getParentUsernames().contains(relative)) {
                       rel.setFamilyId(user.getFamilyId());
                       user.addConnectedUser(relative);
                       rel.addConnectedUser(username);
                       userRepository.save(user);
                       userRepository.save(rel);
                       if (rel.getFcmToken() != null && !rel.getFcmToken().isEmpty()) {
                           webController.newFamilyEventNotification("Du hast eine neue Familienverbindung", user.getUsername() + " hat dich als Mitglied hinzugefügt", rel.getFcmToken());
                       }
                       HttpHeaders header = new HttpHeaders();
                       header.add("familyId", user.getFamilyId());
                       Response response = new Response(relative + " wurde hinzugefügt", 200);
                       return new ResponseEntity<Response>(response, header, HttpStatus.OK);
                   } else {
                       rel.setFamilyId(user.getFamilyId());
                       rel.addConnectedUser(username);
                       userRepository.save(rel);
                       if (rel.getFcmToken() != null && !rel.getFcmToken().isEmpty()) {
                           webController.newFamilyEventNotification("Du hast eine neue Familienverbindung", user.getUsername() + " hat dich als Mitglied hinzugefügt", rel.getFcmToken());
                       }
                       HttpHeaders header = new HttpHeaders();
                       header.add("familyId", user.getFamilyId());
                       Response response = new Response(relative + " wurde hinzugefügt", 200);
                       return new ResponseEntity<Response>(response, header, HttpStatus.OK);
                   }
               } else if ((rel.getFamilyId() != null && !rel.getFamilyId().isEmpty()) && (rel.getParentUsernames() != null && !rel.getParentUsernames().isEmpty())) {
                   if (rel.getFamilyId().equals(user.getFamilyId())) {
                       System.out.println(rel.getFamilyId());
                       System.out.println(user.getFamilyId());
                       if (!user.getParentUsernames().contains(relative)) {
                           if (!rel.getParentUsernames().contains(username)) {
                               System.out.println("NO BODY CONTAINS NOTHING");
                               user.addConnectedUser(relative);
                               rel.addConnectedUser(username);
                               userRepository.save(user);
                               userRepository.save(rel);
                               if (rel.getFcmToken() != null && !rel.getFcmToken().isEmpty()) {
                                   webController.newFamilyEventNotification("Du hast eine neue Familienverbindung", user.getUsername() + " hat dich als Mitglied hinzugefügt", rel.getFcmToken());
                               }
                               HttpHeaders header = new HttpHeaders();
                               header.add("familyId", user.getFamilyId());
                               Response response = new Response(relative + " wurde hinzugefügt", 200);
                               return new ResponseEntity<Response>(response, header, HttpStatus.OK);
                           } else if (rel.getParentUsernames().contains(username)) {
                               user.addConnectedUser(relative);
                               userRepository.save(user);
                               if (rel.getFcmToken() != null && !rel.getFcmToken().isEmpty()) {
                                   webController.newFamilyEventNotification("Du hast eine neue Familienverbindung", user.getUsername() + " hat dich als Mitglied hinzugefügt", rel.getFcmToken());
                               }
                               HttpHeaders header = new HttpHeaders();
                               header.add("familyId", user.getFamilyId());
                               Response response = new Response(relative + " wurde hinzugefügt", 200);
                               return new ResponseEntity<Response>(response, header, HttpStatus.OK);
                           }
                       } else if (user.getParentUsernames().contains(relative)) {
                           if (!rel.getParentUsernames().contains(username)) {
                               rel.addConnectedUser(username);
                               userRepository.save(rel);
                               if (rel.getFcmToken() != null && !rel.getFcmToken().isEmpty()) {
                                   webController.newFamilyEventNotification("Du hast eine neue Familienverbindung", user.getUsername() + " hat dich als Mitglied hinzugefügt", rel.getFcmToken());
                               }
                               HttpHeaders header = new HttpHeaders();
                               header.add("familyId", user.getFamilyId());
                               Response response = new Response(relative + " ist schon in ihrer Familienliste. Die Listen in der Familie sind nun aktualisiert.", 409);
                               return new ResponseEntity<Response>(response, header, HttpStatus.CONFLICT);
                           } else if (rel.getParentUsernames().contains(username)) {
                               HttpHeaders header = new HttpHeaders();
                               header.add("familyId", user.getFamilyId());
                               Response response = new Response(relative + " ist schon in der Familienliste. Die Listen in der Familie sind nun aktualisiert.", 409);
                               return new ResponseEntity<Response>(response, header, HttpStatus.CONFLICT);
                           }
                       }
                   } else {
                       System.out.println(user.getFamilyId() + " != " + rel.getFamilyId());
                       HttpHeaders header = new HttpHeaders();
                       header.add("familyId", user.getFamilyId());
                       Response response = new Response(relative + " ist ein Teil der anderen Familie. Sie müssen sich erst von derzeitiger Familie trennen, um sich mit der anderen zu verbinden.", 409);
                       return new ResponseEntity<Response>(response, header, HttpStatus.CONFLICT);
                   }
               }
            }
        } else {
            System.out.println("1st BAD REQUEST");
            Response response = new Response("Bad Request", 400);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        System.out.println("2nd BAD REQUEST");
        Response response = new Response("Bad Request", 400);
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/detach_from_family")
    public ResponseEntity<Response> detachFamily(@RequestParam("username") String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            Response response = new Response("User wurde nicht gefunden", 400);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            if (!user.getParentUsernames().isEmpty()) {
                for (String str : user.getParentUsernames()) {
                    User usr = userRepository.findByUsername(str);
                    usr.getParentUsernames().remove(username);
                    if (usr.getParentUsernames().isEmpty()) {
                        usr.setFamilyId("");
                    }
                    userRepository.save(usr);
                }
            }
            user.setFamilyId("");
            user.setConnections(new ArrayList<String>());
            userRepository.save(user);
            Response response = new Response("Die Trennung erfolgreich abgeschlossen", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/remove_member")
    public ResponseEntity<Response> removeMember(@RequestParam("username") String username, @RequestParam("relative") String relative) {

        User user = userRepository.findByUsername(username);
        User rel = userRepository.findByUsername(relative);

        if (user == null || rel == null) {
            Response response = new Response("Username nicht gefunden", 400);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else if (user != null && rel != null) {
            //if (user.getFamilyId().equals(rel.getFamilyId())) {
            user.removeConnectedUser(relative);
            rel.removeConnectedUser(username);
            if (user.getParentUsernames().isEmpty()) {
                user.setFamilyId("");
            }
            if (rel.getParentUsernames().isEmpty()) {
                rel.setFamilyId("");
            }
            userRepository.save(user);
            userRepository.save(rel);
            Response response = new Response(relative + " wurde gelöscht", 200);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
        Response response = new Response(relative + " wurde nicht gelöscht. Fehler auf dem Server", 409);
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("get_user_members")
    public ResponseEntity<List<User>> getMembers(@RequestParam("username") String username) {

        User user = userRepository.findByUsername(username);
        List<User> userList = new ArrayList<User>();
        if (user == null) {
            return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        } else if (user.getParentUsernames() == null || user.getParentUsernames().isEmpty()) {
            return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        } else {
            for (String member : user.getParentUsernames()) {
                User memb = userRepository.findByUsername(member);
                if (memb != null) {
                    userList.add(memb);
                }
            }
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @PostMapping("search_users")
    public ResponseEntity<List<String>> searchUsers(@RequestParam("username") String username) {
        List<User> users = userRepository.findByUsernameContains(username);
        List<String> usernames = new ArrayList<String>();

        if (users == null) {
            usernames.add("Nichts gefunden...");
            return new ResponseEntity<List<String>>(usernames, HttpStatus.NOT_FOUND);
        } else if (!users.isEmpty()) {
            for (User user : users) {
                if (user.getUsername().contains(username)) {
                    usernames.add(user.getUsername());
                }
            }
            return new ResponseEntity<List<String>>(usernames, HttpStatus.OK);
        } else {
            return new ResponseEntity<List<String>>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("save_fcm_token")
    public ResponseEntity<Response> saveFcmToken(@RequestParam("username") String username, @RequestParam("fcmToken") String fcmToken) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            Response response = new Response( "User nicht gefunden", 404);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        } else {
            if (user.getFcmToken() == null || user.getFcmToken().isEmpty()) {
                user.setFcmToken(fcmToken);
                userRepository.save(user);
            } else {
                if (!user.getFcmToken().isEmpty() && fcmToken.equals(user.getFcmToken())) {
                    Response response = new Response( "Token aktualisiert", 200);
                    return new ResponseEntity<Response>(response, HttpStatus.OK);
                } else if (!user.getFcmToken().isEmpty() && !fcmToken.equals(user.getFcmToken())) {
                    user.setFcmToken(fcmToken);
                    userRepository.save(user);
                    Response response = new Response( "Token aktualisiert", 200);
                    return new ResponseEntity<Response>(response, HttpStatus.OK);
                }
            }
        }
        Response response = new Response( "Token nicht aktualisiert", 200);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {

        ErrorResponse error = new ErrorResponse();

        error.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());

        error.setMessage(ex.getMessage());

        return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);

    }

    }
