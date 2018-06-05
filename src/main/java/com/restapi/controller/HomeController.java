package com.restapi.controller;

import com.restapi.exception.CustomException;
import com.restapi.security.service.PasswordHandler;
import org.bson.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/hello")
    public String home(){
        return "Hello World!";
    }

    @PostMapping("/pwdtest")
    public void pwdtest(@RequestBody Document doc) throws CustomException {

        String hashed1 = PasswordHandler.securePassword(doc.get("pwd1").toString());
        System.out.println("pwd1 = " + hashed1);
        String hashed1_2 = PasswordHandler.securePassword(doc.get("pwd1").toString());
        System.out.println("pwd1.2 = " + hashed1_2);
        String hashed2 = PasswordHandler.securePassword(doc.get("pwd2").toString());
        System.out.println("pwd2 = " + hashed2);
        if (PasswordHandler.checkPassword(doc.get("pwd1").toString(), hashed1)) {
            System.out.println("hashed.pwd1.toString == pwd1");
        } else {
            System.out.println("hashed.pwd1.toString != pwd1");
        }
        System.out.println("Testing the user.pwd with hashed");
        String user_pwd = "$2a$10$/kmEsKgcXbwTcFzNYqITzOSI4dNCzykcy7vZplpn/CS33KFrU4lUy";
        System.out.println("user_pwd = " + user_pwd);
        String pwd = "ivan";
        String hashedpwd = PasswordHandler.securePassword(pwd);
        if (PasswordHandler.checkPassword(pwd, hashedpwd)) {
            System.out.println("pwd == hashedpwd");
        } else {
            System.out.println("pwd != hashedpwd");
        }
        if (PasswordHandler.checkPassword(pwd, user_pwd)) {
            System.out.println("pwd == user.pwd");
        } else {
            System.out.println("pwd != user.pwd");
        }
        if (PasswordHandler.checkPassword(hashedpwd, user_pwd)) {
            System.out.println("hashedpwd == user.pwd");
        } else {
            System.out.println("hashedpwd != user.pwd");
        }
    }
}