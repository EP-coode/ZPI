package com.core.backend.User;

import com.core.backend.Role.Role;
import com.core.backend.Role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
}

