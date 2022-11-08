package com.core.backend.controller;

import com.core.backend.model.Role;
import com.core.backend.model.User;
import com.core.backend.repository.RoleRepository;
import com.core.backend.repository.UserRepository;
import com.core.backend.exception.NoIdException;
import com.core.backend.utilis.Utilis;
import com.core.backend.exception.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.Arrays.stream;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private Utilis utilis;

    @GetMapping(path = "/{id}")
    ResponseEntity<Object> getUser(@PathVariable(name = "id") String id) {
        // tak można dostać email użytkownika który wysłał zapytanie
        // System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        long longId;
        try {
            longId = utilis.convertId(id);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = userRepository.findById(longId);
        if (userOptional.isEmpty())
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<Object> createUser(@RequestBody User user) {
        if (user == null)
            return new ResponseEntity<>("Zły payload", HttpStatus.BAD_REQUEST);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>("Coś poszło nie tak", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Użytkownik zarejestrowany", HttpStatus.CREATED);
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
        long longId;
        try {
            longId = utilis.convertId(id);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
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


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteUser(@PathVariable String id) {
        long longId;
        try {
            longId = utilis.convertId(id);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsById(longId)) {
            userRepository.deleteById(longId);
            return new ResponseEntity<>("Użytkownik usunięty", HttpStatus.OK);
        }
        return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
    }

}

