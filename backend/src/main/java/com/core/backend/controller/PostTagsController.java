package com.core.backend.controller;

import com.core.backend.dto.mapper.PostMapper;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/post-tags")
public class PostTagsController {

    @Autowired
    PostTagsRepository postTagsRepository;
    @Autowired
    Utilis utils;

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

    @GetMapping("/tags/{startsWithPostTagId}")
    public ResponseEntity<Object> getPostsWithTag(@PathVariable String startsWithPostTagId) {
        Iterable<PostTags> postTags = postTagsRepository.findAll();
        Set<Post> postResultList = new HashSet<>();
        for (PostTags pts : postTags) {
            if(pts.getPrimaryKey().getTagName().getTagName().startsWith(startsWithPostTagId)) {
                postResultList.add(pts.getPrimaryKey().getPostId());
            }
        }
        return new ResponseEntity<>(postResultList.stream().map(PostMapper::toPostDto).collect(Collectors.toList()), HttpStatus.OK);
    }

}
