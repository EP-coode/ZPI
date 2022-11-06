package com.core.backend.controller;

import com.core.backend.dto.PostDto;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.NoIdException;
import com.core.backend.service.PostService;
import com.core.backend.utilis.Utilis;
import com.core.backend.exception.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private Utilis utilis;


    public static final int PAGE_SIZE = 5;

    @GetMapping
    public ResponseEntity<Object> getPosts() {
        return new ResponseEntity<>(postService
                .getAllPosts(), HttpStatus.OK);
    }

    @GetMapping(params = "page")
    public ResponseEntity<Object> getPostsPagination(@RequestParam(required = false) Integer page, Sort.Direction sort) {
        return new ResponseEntity<>(postService
                .getAllPostsPagination(page, sort), HttpStatus.OK);
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<Object> getPost(@PathVariable String postId) {
        PostDto postDto;
        try {
            postDto = postService.getPostByPostId(postId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (NoPostException e) {
            return new ResponseEntity<>("Brak postu o podanym ID", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }


}
