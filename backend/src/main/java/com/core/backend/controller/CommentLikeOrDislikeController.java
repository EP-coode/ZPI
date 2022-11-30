package com.core.backend.controller;

import com.core.backend.exception.NoCommentException;
import com.core.backend.exception.NoIdException;
import com.core.backend.exception.WrongIdException;
import com.core.backend.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(path = "/commentRating")
public class CommentLikeOrDislikeController {
    @Autowired
    private RatingService service;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/like/{commentId}")
    public ResponseEntity<Object> likeComment(@PathVariable String commentId) {
        return likeOrDislikeResponse(commentId, true);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/dislike/{commentId}")
    public ResponseEntity<Object> dislikeComment(@PathVariable String commentId) {
        return likeOrDislikeResponse(commentId, false);
    }

    private ResponseEntity<Object> likeOrDislikeResponse(String commentId, boolean likes) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        RatingService.LikeOrDislikeResult  result;
        String successMessage;
        try {
            result = service.createCommentLikeOrDislike(commentId, email, likes);
            if (result == RatingService.LikeOrDislikeResult.LIKE_OR_DISLIKE_CHANGED)
                successMessage = "Ocena komentarza została zmieniona";
            else if (result == RatingService.LikeOrDislikeResult.LIKE_OR_DISLIKE_DELETED)
                successMessage = "Ocena komentarza została usunięta";
            else
                successMessage = "Komentarz został oceniony";
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }catch(NoCommentException e){
            return new ResponseEntity<>("Podany komentarz nie istnieje", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }
}
