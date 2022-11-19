package com.core.backend.service;

import com.core.backend.dto.AuthTokens;
import com.core.backend.dto.LoginUser;
import com.core.backend.exception.TokenExpiredException;
import com.core.backend.exception.UserDisabledException;
import com.core.backend.model.User;
import com.core.backend.repository.UserRepository;
import com.core.backend.security.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    @Value("${jwt.token.access-token.expiration-time}")
    private int accessTokenExp;

    @Value("${jwt.token.refresh-token.expiration-time}")
    private int refreshTokenExp;

    @Override
    public AuthTokens authenticateUser(LoginUser loginUser) throws JsonProcessingException, UserDisabledException,
            BadCredentialsException {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
        } catch (AuthenticationException ex) {
            if(Objects.equals(ex.getMessage(), "User is disabled"))
                throw new UserDisabledException();
            throw new BadCredentialsException("hasło lub email niepoprawne");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        final String accessToken = tokenProvider.generateAccessToken(user, authorities, accessTokenExp);
        final String refreshToken = tokenProvider.generateRefreshToken(user, refreshTokenExp);
        return new AuthTokens(accessToken, refreshToken);
    }

    @Override
    public AuthTokens refreshTokens(String refreshToken) throws TokenExpiredException {
        String accessToken;
        try {
            String email = tokenProvider.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            User user = userRepository.findByEmail(email);
            accessToken = tokenProvider.generateAccessToken(user, userDetails.getAuthorities(), accessTokenExp);
        }catch (Exception e){
            throw new TokenExpiredException();
        }
        return new AuthTokens(accessToken, refreshToken);
    }
}
