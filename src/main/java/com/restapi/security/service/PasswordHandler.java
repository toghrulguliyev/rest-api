package com.restapi.security.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHandler {

    public static String securePassword(String password){
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashed;
    }

    public static boolean checkPassword(String password, String hashedPassword){
        boolean valid = false;
        if (BCrypt.checkpw(password, hashedPassword)){
            valid = true;
        }
        return valid;
    }

}
