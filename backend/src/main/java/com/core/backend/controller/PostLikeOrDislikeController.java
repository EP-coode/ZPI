package com.core.backend.controller;

import com.core.backend.model.Post;
import com.core.backend.model.PostLikeOrDislike;
import com.core.backend.id.PostLikeOrDislikeId;
import com.core.backend.repository.PostRepository;
import com.core.backend.model.User;
import com.core.backend.repository.UserRepository;
import com.core.backend.repository.PostLikeOrDislikeRepository;
import com.core.backend.exception.NoIdException;
import com.core.backend.utilis.Utilis;
import com.core.backend.exception.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(path = "/postLikeOrDislike")
public class PostLikeOrDislikeController {

    @Autowired
    private PostLikeOrDislikeRepository postLikeOrDislikeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private Utilis utilis;

    @GetMapping("/postId={postId}&userId={userId}")
    public ResponseEntity<Object> getLike(@PathVariable String postId, @PathVariable String userId) {
        long postLongId;
        long userLongId;
        try {
            postLongId = utilis.convertId(postId);
            userLongId = utilis.convertId(userId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        Optional<User> userToFind = userRepository.findById(userLongId);
        if (userToFind.isEmpty())
            return new ResponseEntity<>("Użytkownik o podanym id nie istnieje", HttpStatus.BAD_REQUEST);
        Optional<Post> postToFind = postRepository.findById(postLongId);
        if (postToFind.isEmpty())
            return new ResponseEntity<>("Post o podanym id nie istnieje", HttpStatus.BAD_REQUEST);
        User user = userToFind.get();
        Post post = postToFind.get();
        PostLikeOrDislikeId postLikeOrDislikeId = new PostLikeOrDislikeId(user, post);
        Optional<PostLikeOrDislike> postLikeOrDislike = postLikeOrDislikeRepository.findById(postLikeOrDislikeId);
        if (postLikeOrDislike.isEmpty())
            return new ResponseEntity<>("Brak postu o takim id, użytkownika o takim id", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(postLikeOrDislike.get(), HttpStatus.OK);
    }
}
