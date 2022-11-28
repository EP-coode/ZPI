package com.core.backend.controller;

import com.core.backend.dto.user.UserDto;
import com.core.backend.dto.mapper.UserMapper;
import com.core.backend.exception.NoUserException;
import com.core.backend.exception.NoIdException;
import com.core.backend.service.UserService;
import com.core.backend.exception.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "followedUsers")
public class FollowedUserController {

    @Autowired
    UserService userService;


    @GetMapping(path = "/followers/{userId}")
    public ResponseEntity<Object> getFollowers(@PathVariable String userId) {
        try {
            List<UserDto> result = userService.getFollowers(userId).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }catch(NoUserException e){
            return new ResponseEntity<>("Podany użytkownik nie istnieje", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/followings/{userId}")
    public ResponseEntity<Object> getFollowings(@PathVariable String userId) {
        try {
            List<UserDto> result = userService.getFollowings(userId).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }catch(NoUserException e){
            return new ResponseEntity<>("Podany użytkownik nie istnieje", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/followUnfollow/{userId}")
    public ResponseEntity<Object> followUnfollow(@PathVariable String userId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            userService.followUnfollowUser(email, userId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }catch(NoUserException e){
            return new ResponseEntity<>("Podany użytkownik nie istnieje", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sukces", HttpStatus.OK);
    }
}
