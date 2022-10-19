package com.core.backend.FollowedUser;

import com.core.backend.User.User;
import com.core.backend.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "followedUser")
public class FollowedUserController {
    @Autowired
    private FollowedUserRepository followedUserRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "myFollowers/{userId}")
    public ResponseEntity<Object> getMyFollowers(@PathVariable String userId) {
        if (userId == null)
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        long longId;
        try {
            longId = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        List<FollowedUser> followedUserList = followedUserRepository.findAll();
        List<User> result = new ArrayList<>();
        for (FollowedUser followedUser : followedUserList) {
            if (followedUser.getPrimaryKey().getFollowedUserId().getUserId() == longId)
                result.add(userRepository.findById(followedUser.getPrimaryKey().getUserId().getUserId()).orElseThrow());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(path = "myFollowings/{userId}")
    public ResponseEntity<Object> getMyFollowing(@PathVariable String userId) {
        if (userId == null)
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        long longId;
        try {
            longId = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        List<FollowedUser> followedUserList = followedUserRepository.findAll();
        List<User> result = new ArrayList<>();
        for (FollowedUser followedUser : followedUserList) {
            if (followedUser.getPrimaryKey().getUserId().getUserId() == longId)
                result.add(userRepository.findById(followedUser.getPrimaryKey().getFollowedUserId().getUserId()).orElseThrow());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
