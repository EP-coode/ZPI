package com.core.backend.controller;

import com.core.backend.model.Post;
import com.core.backend.model.PostTags;
import com.core.backend.repository.PostTagsRepository;
import com.core.backend.exception.NoIdException;
import com.core.backend.utilis.Utilis;
import com.core.backend.exception.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/post-tags")
public class PostTagsController {

    @Autowired
    PostTagsRepository postTagsRepository;
    @Autowired
    Utilis utils;

    //not that useful
    @GetMapping()
    public ResponseEntity<Object> getAllPostTags() {
        return new ResponseEntity<>(postTagsRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Object> getPostTags(@PathVariable String postId) {
        long longId;
        try {
            longId = utils.convertId(postId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        Iterable<PostTags> postTags = postTagsRepository.findAll();
        List<String> postTagResultList = new ArrayList<>();
        for (PostTags pts : postTags) {
            if(pts.getPrimaryKey().getPostId().getPostId() == longId) {
                postTagResultList.add(pts.getPrimaryKey().getTagName().getTagName());
            }
        }
        return new ResponseEntity<>(postTagResultList, HttpStatus.OK);
    }

    //to-do (maybe) ?       PathVar array&multiple tags
    @GetMapping("/tags/{postTagId}")
    public ResponseEntity<Object> getPostsWithTag(@PathVariable String postTagId) {
        Iterable<PostTags> postTags = postTagsRepository.findAll();
        List<Post> postResultList = new ArrayList<>();
        for (PostTags pts : postTags) {
            if(pts.getPrimaryKey().getTagName().getTagName().equals(postTagId)) {
                postResultList.add(pts.getPrimaryKey().getPostId());
            }
        }
        return new ResponseEntity<>(postResultList, HttpStatus.OK);
    }

}
