package com.core.backend.PostLikeOrDislike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "postLikeOrDislike")
public class PostLikeOrDislikeController {

    @Autowired
    private PostLikeOrDislikeRepository postLikeOrDislikeRepository;

    @GetMapping("/postId={postId}&userId={userId}")
    public ResponseEntity<Object> getLike(@PathVariable String postId, @PathVariable String userId) throws NoSuchMethodException {

        throw new NoSuchMethodException();
        //TODO To Implement
    }
}
