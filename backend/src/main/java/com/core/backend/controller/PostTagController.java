package com.core.backend.controller;

import com.core.backend.dto.TagsDto;
import com.core.backend.model.PostTag;
import com.core.backend.repository.PostTagRepository;
import com.core.backend.utilis.Utilis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(path = "/post-tag")
public class PostTagController {

    @Autowired
    PostTagRepository postTagRepository;
    @Autowired
    Utilis utils;

    public static final int DEFAULT_TAGS_LIMIT = 7;

    // @GetMapping()
    // public ResponseEntity<Object> getAllPostTags() {
    // return new ResponseEntity<>(postTagRepository.findAll(), HttpStatus.OK);
    // }

    // @GetMapping(path = "/{postTagId}")
    // public ResponseEntity<Object> getPostCategoryGroup(@PathVariable String
    // postTagId) {
    // Optional<PostTag> postTag = postTagRepository.findById(postTagId);
    // if (postTag.isEmpty())
    // return new ResponseEntity<>("Tag o podanym id nie istnieje",
    // HttpStatus.BAD_REQUEST);
    // return new ResponseEntity<>(postTag.get(), HttpStatus.OK);
    // }

    @GetMapping
    public ResponseEntity<Object> getTagsByPrefix(@RequestParam(required = true) String tagPrefix,
            @RequestParam(required = false) Integer limit) {
        Integer _limit = limit == null ? DEFAULT_TAGS_LIMIT : limit;
        Pageable pageable = PageRequest.of(0, _limit);
        List<PostTag> result = postTagRepository.getTagsByPrefix(tagPrefix, pageable);

        return new ResponseEntity<>(new TagsDto(result), HttpStatus.OK);
    }

}
