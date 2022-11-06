package com.core.backend.controller;

import com.core.backend.model.FollowedCategories;
import com.core.backend.repository.FollowedCategoriesRepository;
import com.core.backend.model.PostCategory;
import com.core.backend.model.User;
import com.core.backend.repository.UserRepository;
import com.core.backend.exception.NoIdException;
import com.core.backend.utilis.Utilis;
import com.core.backend.exception.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "followedCategories")
public class FollowedCategoriesController {

    @Autowired
    private FollowedCategoriesRepository followedCategoriesRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utilis utilis;

    @GetMapping("{userId}")
    public ResponseEntity<Object> getAllFollowedCategories(@PathVariable String userId) {

        long longId;
        try {
            longId = utilis.convertId(userId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findById(longId);
        if (user.isEmpty())
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        List<FollowedCategories> followedCategoriesList = followedCategoriesRepository.findAll();
        List<PostCategory> result = new ArrayList<>();
        for (FollowedCategories followedCategories : followedCategoriesList) {
            if (followedCategories.getPrimaryKey().getUser().getUserId() == longId)
                result.add(followedCategories.getPrimaryKey().getPostCategory());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
