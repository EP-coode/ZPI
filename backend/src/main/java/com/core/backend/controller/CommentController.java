package com.core.backend.controller;

import com.core.backend.dto.CommentDto;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.NoIdException;
import com.core.backend.service.PostService;
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

import java.util.List;

@Controller
@RequestMapping(path = "/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private PostService postService;


    @GetMapping()
    public ResponseEntity<Object> getComments(@PathVariable String postId) {
        List<CommentDto> commentDtos;
        try {
            commentDtos = postService.getComments(postId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (NoPostException e) {
            return new ResponseEntity<>("Brak postu o podanym ID", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @GetMapping(params = "page")
    public ResponseEntity<Object> getCommentsPagination(@PathVariable String postId, @RequestParam(required = false) Integer page, Sort.Direction sort) {
        List<CommentDto> commentDtos;
        try {
            commentDtos = postService.getCommentsPagination(postId, page, sort);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (NoPostException e) {
            return new ResponseEntity<>("Brak postu o podanym ID", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

}
