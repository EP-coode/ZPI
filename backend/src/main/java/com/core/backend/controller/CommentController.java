package com.core.backend.controller;

import com.core.backend.dto.comment.CommentCreateUpdateDto;
import com.core.backend.dto.comment.CommentDto;
import com.core.backend.dto.comment.CommentWithPaginationDto;
import com.core.backend.exception.NoCommentException;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.NoIdException;
import com.core.backend.service.PostService;
import com.core.backend.exception.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(path = "/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private PostService postService;

    @GetMapping(value = "/{commentId}")
    public ResponseEntity<Object> getComment(@PathVariable String commentId) {
        CommentDto commentDto;
        try {
            commentDto = postService.getCommentById(commentId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (NoCommentException e) {
            return new ResponseEntity<>("Brak komentarza o podanym ID", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

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
    public ResponseEntity<Object> getCommentsPagination(@PathVariable String postId,
            @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
            Sort.Direction sort) {
        CommentWithPaginationDto commentDtos;
        try {
            commentDtos = postService.getCommentsPagination(postId, page, pageSize, sort);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (NoPostException e) {
            return new ResponseEntity<>("Brak postu o podanym ID", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping()
    public ResponseEntity<Object> addComment(@PathVariable String postId, @Valid @RequestBody CommentCreateUpdateDto commentCreateUpdateDto) {
        CommentDto comment;
        try {
            comment = postService.addComment(postId, commentCreateUpdateDto);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (NoPostException e) {
            return new ResponseEntity<>("Brak postu o podanym ID", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable("postId") String postId, @PathVariable("commentId") String commentId,
                                                @Valid @RequestBody CommentCreateUpdateDto commentCreateUpdateDto) {
        try {
            postService.updateComment(postId, commentId, commentCreateUpdateDto);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Zaktualizowano komentarz", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable String commentId) {
        try {
            postService.deleteComment(commentId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        HashMap<String, String> result = new HashMap<>();
        result.put("message", "Komentarz usutnięto pomyślnie");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
