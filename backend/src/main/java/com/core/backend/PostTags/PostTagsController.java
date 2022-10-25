package com.core.backend.PostTags;

import com.core.backend.FollowedCategories.FollowedCategories;
import com.core.backend.Post.Post;
import com.core.backend.PostCategory.PostCategory;
import com.core.backend.PostTag.PostTag;
import com.core.backend.User.User;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "postTags")
public class PostTagsController {

    @Autowired
    PostTagsRepository postTagsRepository;
    @Autowired
    Utilis utils;

    @GetMapping("post/{postId}")
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
    @GetMapping("tag/{postTagId}")
    public ResponseEntity<Object> getPostsWithTag(@PathVariable String postTagId) {
        Iterable<PostTags> postTags = postTagsRepository.findAll();
        List<String> postResultList = new ArrayList<>();
        for (PostTags pts : postTags) {
            if(pts.getPrimaryKey().getTagName().equals(postTagId)) {
                postResultList.add(pts.getPrimaryKey().getTagName().getTagName());
            }
        }
        return new ResponseEntity<>(postResultList, HttpStatus.OK);
    }

}
