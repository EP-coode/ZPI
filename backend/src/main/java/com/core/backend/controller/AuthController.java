package com.core.backend.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.core.backend.dto.AuthTokens;
import com.core.backend.exception.UserDisabledException;
import com.core.backend.dto.user.LoginUser;
import com.core.backend.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Controller
@RequestMapping(path = "/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping("/login")
    ResponseEntity<Object> login(@Valid @RequestBody LoginUser loginUser)
            throws AuthenticationException, JsonProcessingException, IllegalArgumentException, JWTCreationException {
        AuthTokens tokens;
        try{
            tokens = service.authenticateUser(loginUser);
        }catch (UserDisabledException e){
            return new ResponseEntity<>("Użytkownik niepotwierdzony", HttpStatus.BAD_REQUEST);
        }catch(BadCredentialsException e){
            return new ResponseEntity<>("Niepoprawne email lub hasło", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @GetMapping("/refresh_token")
    ResponseEntity<Object> refreshToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                AuthTokens tokens = service.refreshTokens(refreshToken);
                return new ResponseEntity<>(tokens, HttpStatus.OK);
            } catch (Exception exception) {
                return new ResponseEntity<>(exception.getMessage(), FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Nie znaleziono refresh tokenu", HttpStatus.BAD_REQUEST);
        }
    }
}
