package com.restapi.converter;

import com.restapi.model.Role;
import com.restapi.model.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class UserDTOConverter {

    public static Document userToDocument(User user) {
        Document doc = new Document();

        doc.append("firstName", user.getFirstName());
        doc.append("lastName", user.getLastName());
        doc.append("username", user.getUsername());
        doc.append("password", user.getPassword());
        doc.append("accType", user.getAccType());
        doc.append("enabled", user.isEnabled());
        doc.append("email", user.getEmail());
        doc.append("birthday", user.getBirthday());
        doc.append("gender", user.getGender());
        doc.append("relativeUsernames", user.getParentUsernames());

        return doc;
    }

    public static User documentToUser(Document doc) {

        String firstName = (String) doc.get("firstName");
        String lastName = (String) doc.get("lastName");
        String username = (String) doc.get("username");
        String password = (String) doc.get("password");
        int accType = (int) doc.get("accType");
        boolean enabled = (boolean) doc.get("enabled");
        String email = (String) doc.get("email");
        int birthday = (int) doc.get("birthday");
        String gender = (String) doc.get("gender");
        User user = new User(firstName, lastName, username, email, password.trim(), birthday, gender, enabled, accType);
        //user.addAuthority(Role.USER);
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.USER);
        user.setAuthorities(roles);
        return user;
    }
}
