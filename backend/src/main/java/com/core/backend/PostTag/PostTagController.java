package com.core.backend.PostTag;

import com.core.backend.PostCategoryGroup.PostCategoryGroup;
import com.core.backend.utilis.NoIdException;
import com.core.backend.utilis.Utilis;
import com.core.backend.utilis.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Controller
@RequestMapping(path = "postTag")
public class PostTagController {

    @Autowired
    PostTagRepository postTagRepository;
    @Autowired
    Utilis utils;

    @GetMapping()
    public ResponseEntity<Object> getAllPostTags() {
        return new ResponseEntity<>(postTagRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "{postTagId}")
    public ResponseEntity<Object> getPostCategoryGroup(@PathVariable String postTagId) {
        Optional<PostTag> postTag = postTagRepository.findById(postTagId);
        if (postTag.isEmpty())
            return new ResponseEntity<>("Tag o podanym id nie istnieje", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(postTag.get(), HttpStatus.OK);
    }

}
