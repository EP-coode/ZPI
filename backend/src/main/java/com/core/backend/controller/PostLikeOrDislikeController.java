package com.core.backend.controller;

import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoPostException;
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
@RequestMapping(path = "/postRating")
public class PostLikeOrDislikeController {

    @Autowired
    private RatingService service;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/like/{postId}")
    public ResponseEntity<Object> likePost(@PathVariable String postId) {
        return likeOrDislikeResponse(postId, true);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/dislike/{postId}")
    public ResponseEntity<Object> dislikePost(@PathVariable String postId) {
        return likeOrDislikeResponse(postId, false);
    }

    private ResponseEntity<Object> likeOrDislikeResponse(String postId, boolean likes) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        RatingService.LikeOrDislikeResult  result;
        String successMessage;
        try {
            result = service.createPostLikeOrDislike(postId, email, likes);
            if (result == RatingService.LikeOrDislikeResult.LIKE_OR_DISLIKE_CHANGED)
                successMessage = "Ocena postu została zmieniona";
            else if (result == RatingService.LikeOrDislikeResult.LIKE_OR_DISLIKE_DELETED)
                successMessage = "Ocena postu została usunięta";
            else
                successMessage = "Post został oceniony";
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }catch(NoPostException e){
            return new ResponseEntity<>("Podany post nie istnieje", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

//    @GetMapping("/postId={postId}&userId={userId}")
//    public ResponseEntity<Object> getLike(@PathVariable String postId, @PathVariable String userId) {
//        long postLongId;
//        long userLongId;
//        try {
//            postLongId = utilis.convertId(postId);
//            userLongId = utilis.convertId(userId);
//        } catch (WrongIdException e) {
//            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
//        } catch (NoIdException e) {
//            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
//        }
//        Optional<User> userToFind = userRepository.findById(userLongId);
//        if (userToFind.isEmpty())
//            return new ResponseEntity<>("Użytkownik o podanym id nie istnieje", HttpStatus.BAD_REQUEST);
//        Optional<Post> postToFind = postRepository.findById(postLongId);
//        if (postToFind.isEmpty())
//            return new ResponseEntity<>("Post o podanym id nie istnieje", HttpStatus.BAD_REQUEST);
//        User user = userToFind.get();
//        Post post = postToFind.get();
//        PostLikeOrDislikeId postLikeOrDislikeId = new PostLikeOrDislikeId(user, post);
//        Optional<PostLikeOrDislike> postLikeOrDislike = postLikeOrDislikeRepository.findById(postLikeOrDislikeId);
//        if (postLikeOrDislike.isEmpty())
//            return new ResponseEntity<>("Brak postu o takim id, użytkownika o takim id", HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(postLikeOrDislike.get(), HttpStatus.OK);
//    }


}
