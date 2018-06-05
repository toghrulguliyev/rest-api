package com.restapi.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface TokenAuthenticationService {

    Authentication authenticate(HttpServletRequest request);

}
