package com.core.backend.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.core.backend.security.TokenProvider;
import com.core.backend.dto.LoginUser;
import com.core.backend.model.User;
import com.core.backend.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenProvider tokenProvider;

    @Value("${jwt.token.access-token.expiration-time}")
    private int accessTokenExp;

    @Value("${jwt.token.refresh-token.expiration-time}")
    private int refreshTokenExp;

    @PostMapping("/login")
    ResponseEntity<Object> login(@Valid @RequestBody LoginUser loginUser, BindingResult result)
            throws AuthenticationException, JsonProcessingException, IllegalArgumentException, JWTCreationException {
        if (result.hasErrors()) {
            return new ResponseEntity<>("Niepoprawne dane logowania", HttpStatus.BAD_REQUEST);
        }
        String email = loginUser.getEmail();
        String password = loginUser.getPassword();
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = authentication.getName();
        User user = userService.getUserByEmail(username);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        final String accessToken = tokenProvider.generateAccessToken(user, authorities, accessTokenExp);
        final String refreshToken = tokenProvider.generateRefreshToken(user, refreshTokenExp);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @GetMapping("/refresh_token")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                String email = tokenProvider.getUsernameFromToken(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                User user = userService.getUserByEmail(email);
                String accessToken = tokenProvider.generateAccessToken(user, userDetails.getAuthorities(),
                        accessTokenExp);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Nie znaleziono refresh tokenu");
        }
    }
}
