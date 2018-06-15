package com.restapi.security.service;

import com.restapi.model.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenHandler{

    private static int tokenExpirationTime = 20;

    @Value("security.token.secret.key")
    private String tokenKey;

//    @Autowired
//    public TokenHandler() {
//    }


    public String generateToken(final User user, ObjectId id) {
        if (user == null) {
            return null;
        } else {
            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("clientType", user.getAccType());
            //tokenData.put("userID", id);
            tokenData.put("username", user.getUsername());
            tokenData.put("token_create_date", LocalDateTime.now());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, tokenExpirationTime);
            tokenData.put("token_expiration_date", calendar.getTime());
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setClaims(tokenData);
            jwtBuilder.setId(id.toString());
            jwtBuilder.setExpiration(calendar.getTime());
            return jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenKey).compact();
        }
    }

    public static void setTokenExpirationTime(final int tokenExpirationTime) {
        TokenHandler.tokenExpirationTime = tokenExpirationTime;
    }
}
