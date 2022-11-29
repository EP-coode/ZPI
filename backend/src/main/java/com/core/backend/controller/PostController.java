package com.core.backend.controller;

import com.core.backend.dto.filter.PostFilters;
import com.core.backend.dto.mapper.PostMapper;
import com.core.backend.dto.post.PostCreateUpdateDto;
import com.core.backend.dto.post.PostDto;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.NoIdException;
import com.core.backend.service.PostService;
import com.core.backend.exception.WrongIdException;
import com.core.backend.model.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Controller
@RequestMapping(path = "/posts")
public class PostController {

    @Autowired
    private PostService postService;

    public static final int DEFAULT_POST_PAGE_SIZE = 7;

    @PostMapping
    public ResponseEntity<Object> getPostsFiltered(@RequestBody PostFilters postFilters,
            @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {

        int _page = page == null ? 0 : page;
        int _pageSize = pageSize == null ? DEFAULT_POST_PAGE_SIZE : pageSize;

        return new ResponseEntity<>(postService
                .getPostsFiltered(postFilters, _page, _pageSize), HttpStatus.OK);
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
    @RequestMapping(path = "/create", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> addPost(@ModelAttribute PostCreateUpdateDto post) {
        try {
            PostDto createdPost = postService.addPost(post);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
        byte[] photo;
        try {
            photo = postService.getPhotoByPostId(postId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (NoPostException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        long contentLength = photo == null ? 0 : photo.length;
        return ResponseEntity.ok()
                .contentLength(contentLength)
                .contentType(MediaType.IMAGE_PNG)
                .body(photo);
    }

}
