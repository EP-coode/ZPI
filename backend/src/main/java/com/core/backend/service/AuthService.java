package com.core.backend.service;

import com.core.backend.dto.AuthTokens;
import com.core.backend.dto.user.LoginUser;
import com.core.backend.exception.TokenExpiredException;
import com.core.backend.exception.UserDisabledException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.authentication.BadCredentialsException;

public interface AuthService {
    AuthTokens authenticateUser(LoginUser loginUser) throws JsonProcessingException, UserDisabledException, BadCredentialsException;
    AuthTokens refreshTokens(String refreshToken) throws JsonProcessingException, TokenExpiredException;
}
