package com.core.backend.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.core.backend.Role.Role;
import com.core.backend.Role.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(path = "/{id}")
    ResponseEntity<Object> getUser(@PathVariable(name = "id") String id) {
        if (id != null) {
            long longId;
            try {
                longId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
            }
            Optional<User> userOptional = userRepository.findById(longId);
            if (userOptional.isPresent())
                return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
    }

    @PostMapping()
    ResponseEntity<Object> createUser(@RequestBody User user) {
        if (user != null) {
            try {
                userRepository.save(user);
            } catch (Exception e) {
                return new ResponseEntity<>("Coś poszło nie tak", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Użytkownik zarejestrowany", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Zły payload", HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Object> updateUser(@PathVariable(name = "id") String id,
                                      @RequestParam String roleName,
                                      @RequestParam String avatarUrl,
                                      @RequestParam String email,
                                      @RequestParam String emailConfirmationToken,
                                      @RequestParam Boolean emailConfirmed,
                                      @RequestParam String refreshToken,
                                      @RequestParam Boolean studentStatusConfirmed) {
        if (id != null) {
            long longId;
            try {
                longId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
            }
            Optional<User> userOptional = userRepository.findById(longId);
            if (userOptional.isEmpty())
                return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
            User userToSave = userOptional.get();
            if (roleName != null) {
                Optional<Role> roleOptional = roleRepository.findById(roleName);
                if (roleOptional.isEmpty())
                    return new ResponseEntity<>("Brak roli o danym id", HttpStatus.NOT_FOUND);
                userToSave.setRole(roleOptional.get());
            }
            userToSave.setAvatarUrl(avatarUrl); // użytkownik może usunąć zdjęcie avatara
            if (email != null)
                userToSave.setEmail(email);
            if (emailConfirmationToken != null)
                userToSave.setEmailConfirmationToken(emailConfirmationToken);
            if (emailConfirmed != null)
                userToSave.setEmailConfirmed(emailConfirmed);
            if (refreshToken != null)
                userToSave.setRefreshToken(refreshToken);
            if (studentStatusConfirmed != null)
                userToSave.setStudentStatusConfirmed(studentStatusConfirmed);
            userRepository.save(userToSave);
            return new ResponseEntity<>("Użytkownik zaktualizowany", HttpStatus.OK);
        }
        return new ResponseEntity<>("Zły payload", HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteUser(@PathVariable String id) {
        if (id != null) {
            long longId;
            try {
                longId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
            }
            if (userRepository.existsById(longId)) {
                userRepository.deleteById(longId);
                return new ResponseEntity<>("Użytkownik usunięty", HttpStatus.OK);
            }
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/refresh_token")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                // trzeba zamienić secret na jakiś faktyczny klucz
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String email = decodedJWT.getSubject();
                User user = userRepository.findByEmail(email);
                String accessToken = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", Arrays.asList(user.getRole().getRoleName()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_toke", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception){
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}

