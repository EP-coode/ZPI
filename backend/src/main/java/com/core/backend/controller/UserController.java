package com.core.backend.controller;

import com.core.backend.dto.user.UserDto;
import com.core.backend.dto.mapper.UserDetailsMapper;
import com.core.backend.dto.mapper.UserMapper;
import com.core.backend.exception.NoRoleException;
import com.core.backend.exception.NoUserException;
import com.core.backend.model.Role;
import com.core.backend.model.User;
import com.core.backend.exception.NoIdException;
import com.core.backend.service.FileService;
import com.core.backend.service.UserService;
import com.core.backend.utilis.Utilis;
import com.core.backend.exception.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private Utilis utilis;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;


    @GetMapping()
    ResponseEntity<Object> getAllUsersPagination(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) Integer page,
                                                 @RequestParam(defaultValue = "DESC") Sort.Direction sort) {
        try {
            List<UserDto> users = userService.getAllUsers(name, page, sort).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<Object> getUser(@PathVariable(name = "id") String id) {
        User user;
        try {
            user = userService.getUserById(id);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }catch(NoUserException e) {
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(UserMapper.toUserDto(user), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/details")
    public ResponseEntity<Object> getUserDetails() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        return new ResponseEntity<>(UserDetailsMapper.toUserDetails(user), HttpStatus.OK);
    }

    @GetMapping(path = "/avatar/{name}")
    public ResponseEntity<Object> getAvatar(@PathVariable(name = "name") String name) {
        User user = userService.getUserByName(name);
        if(user == null)
            return new ResponseEntity<>("Użytkownik nie istnieje", HttpStatus.NOT_FOUND);
        if(user.getAvatarUrl() == null)
            return new ResponseEntity<>("Użytkownik nie posiada avataru", HttpStatus.BAD_REQUEST);
        try {
            byte[] image = fileService.downloadFile(user.getAvatarUrl());
            return ResponseEntity.ok()
                    .contentLength(image.length)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(image);
        }catch(Exception e) {
            return new ResponseEntity<>("Wystąpił błąd przy pobieraniu zdjęcia", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/avatar")
    public ResponseEntity<Object> changeAvatar(@RequestParam("file") MultipartFile file) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String extension = utilis.getExtensionByStringHandling(file.getOriginalFilename()).get();
        userService.changeAvatar(email, extension);
        User user = userService.getUserByEmail(email);
        try {
            fileService.uploadFile(file, user.getAvatarUrl());
        }catch(Exception e){
            userService.deleteAvatar(email);
            return new ResponseEntity<>("nie udało się zmienić zdjęcia profilowego", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("zdjęcie profilowe zmienione", HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/avatar")
    public ResponseEntity<Object> deleteAvatar() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String filename = userService.getUserByEmail(email).getAvatarUrl();
        try {
            fileService.deleteFile(filename);
            userService.deleteAvatar(email);
        }catch(Exception e){
            return new ResponseEntity<>("nie udało się usunąć zdjęcia profilowego", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("zdjęcie profilowe usunięte", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/adminPanel")
    ResponseEntity<Object> createUser(@RequestBody User user) {
        if (user == null)
            return new ResponseEntity<>("Zły payload", HttpStatus.BAD_REQUEST);
        try {
            userService.saveUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>("Coś poszło nie tak", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Użytkownik zarejestrowany", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/adminPanel/{id}")
    ResponseEntity<Object> updateFullUser(@PathVariable(name = "id") String id,
                                      @RequestParam String roleName,
                                      @RequestParam String avatarUrl,
                                      @RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String emailConfirmationToken,
                                      @RequestParam Boolean emailConfirmed,
                                      @RequestParam String refreshToken,
                                      @RequestParam Boolean pwrStatusConfirmed) {
        User userToSave;
        try {
            userToSave = userService.getUserById(id);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }catch(NoUserException e) {
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        }
        if (roleName != null) {
            Role role;
            try{
                role = userService.getRole(roleName);
            }catch(NoRoleException e) {
                return new ResponseEntity<>("Brak roli o danym id", HttpStatus.NOT_FOUND);
            }
            userToSave.setRole(role);
        }
        userToSave.setAvatarUrl(avatarUrl); // użytkownik może usunąć zdjęcie avatara
        if (email != null)
            userToSave.setEmail(email);
        if (name != null)
            userToSave.setName(name);
        if (emailConfirmationToken != null)
            userToSave.setEmailConfirmationToken(emailConfirmationToken);
        if (emailConfirmed != null)
            userToSave.setEmailConfirmed(emailConfirmed);
        if (refreshToken != null)
            userToSave.setRefreshToken(refreshToken);
        if (pwrStatusConfirmed != null)
            userToSave.setPwrStatusConfirmed(pwrStatusConfirmed);
        userService.saveUser(userToSave);
        return new ResponseEntity<>("Użytkownik zaktualizowany", HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/adminPanel/{id}")
    ResponseEntity<Object> deleteUser(@PathVariable String id) {
        User userTodelete;
        try {
            userTodelete = userService.getUserById(id);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }catch(NoUserException e) {
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(userTodelete);
        return new ResponseEntity<>("Użytkownik usunięty", HttpStatus.OK);
    }
}

