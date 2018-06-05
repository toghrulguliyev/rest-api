package com.restapi.security.service;


import com.restapi.exception.UserNotFoundException;
import com.restapi.model.User;
import com.restapi.model.UserAuthentication;
import com.restapi.security.constants.SecurityConst;
import com.restapi.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenAuthentication implements TokenAuthenticationService {

    @Value("security.token.secret.key")
    private String secretKey;

    @Autowired
    private UserService userService;

    @Autowired
    private final TokenHandler tokenHandler = new TokenHandler();

//    @Autowired
//    public TokenAuthentication(final TokenService tokenService) {
//        this.tokenService = tokenService;
//    }

    @Override
    public Authentication authenticate(final HttpServletRequest request) {

        final String token = request.getHeader(SecurityConst.AUTH_HEADER_NAME);


        final Jws<Claims> tokenData = parseToken(token);

        if (tokenData != null) {
            User user = getUserFromToken(tokenData);

            //if (user.isAccountNonExpired())

            if (user != null) {

                return new UserAuthentication(user);
            }
        }
        return null;
    }

    private Jws<Claims> parseToken(final String token) {
        if (token != null) {
            try {
                return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                    | SignatureException | IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    private User getUserFromToken(final Jws<Claims> tokenData) {
        try {
            return (User) userService
                    .loadUserByUsername(tokenData.getBody().get("username").toString());
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("User "
                    + tokenData.getBody().get("username").toString() + " not found");
        }
    }

}
