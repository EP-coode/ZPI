package com.core.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    @Value("${jwt.token.issuer}")
    public String ISSUER;

    @Value("${jwt.token.signing.key}")
    public String SIGNING_KEY;

    public String getUsernameFromToken(String token){
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getSubject();
    }

    public String getRoleFromToken(String token){
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getClaim("roles").asArray(String.class)[0];
    }

    public Date getExpirationDateFromToken(String token){
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getExpiresAt();
    }

    private DecodedJWT getDecodedJWT(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SIGNING_KEY.getBytes())).build();
        return verifier.verify(token);
    }

    public String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities, int expirationTime){
        return generateToken(username, authorities, expirationTime);
    }

    public String generateRefreshToken(String username, int expirationTime){
        return generateToken(username,null, expirationTime);
    }

    private String generateToken(String username, Collection<? extends GrantedAuthority> authorities, int expirationTime){
        JWTCreator.Builder tokenBuilder = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withIssuer(ISSUER);
        if(authorities != null)
            return tokenBuilder
                    .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(Algorithm.HMAC256(SIGNING_KEY.getBytes()));
        else
            return tokenBuilder.sign(Algorithm.HMAC256(SIGNING_KEY.getBytes()));
    }

}
