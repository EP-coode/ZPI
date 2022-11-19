package com.core.backend.controller;

import com.core.backend.dto.filter.PostFilters;
import com.core.backend.dto.post.PostCreateUpdateDto;
import com.core.backend.dto.post.PostDto;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.NoIdException;
import com.core.backend.model.User;
import com.core.backend.service.FileService;
import com.core.backend.service.PostService;
import com.core.backend.utilis.Utilis;
import com.core.backend.exception.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping(path = "/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private Utilis utilis;


    public static final int PAGE_SIZE = 5;

//    @GetMapping
//    public ResponseEntity<Object> getPosts() {
//        return new ResponseEntity<>(postService
//                .getAllPosts(), HttpStatus.OK);
//    }
//
//    @GetMapping(params = "page")
//    public ResponseEntity<Object> getPostsPagination(@RequestParam(required = false) Integer page, Sort.Direction sort) {
//        return new ResponseEntity<>(postService
//                .getAllPostsPagination(page, sort), HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<Object> getPostsFiltered(@RequestBody PostFilters postFilters) {
        return new ResponseEntity<>(postService
                .getPostsFiltered(postFilters), HttpStatus.OK);
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Transactional
    @PostMapping
    public ResponseEntity<Object> addPost(@RequestBody PostCreateUpdateDto postCreateDto, @RequestBody(required = false) MultipartFile photo) {
        try {
            postCreateDto = postService.addPost(postCreateDto, photo);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(postCreateDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Transactional
    @PutMapping("/{postId}")
    public ResponseEntity<Object> updatePost(@PathVariable String postId, @RequestBody PostCreateUpdateDto postDto,
                                             @RequestBody(required = false) MultipartFile photo) {
        try {
            postService.updatePost(postId, postDto, photo);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Zaktualizowano post", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Transactional
    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable String postId) {
        try {
            postService.deletePost(postId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Post usunięto pomyślnie", HttpStatus.OK);
    }


    @GetMapping("/{postId}/photo")
    public ResponseEntity<Object> getPhotoByPostId(@PathVariable String postId) {
        ByteArrayResource photo;
        try {
            photo = postService.getPhotoByPostId(postId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (NoPostException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        long contentLength = photo == null ? 0 : photo.contentLength();
        return ResponseEntity.ok()
                .contentLength(contentLength)
                .contentType(MediaType.IMAGE_PNG)
                .body(photo);
    }





}
