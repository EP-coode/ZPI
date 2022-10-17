package com.core.backend.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(path = "/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<Object> getPost() {
        return new ResponseEntity<>(postRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getPost(@PathVariable String id) {
        if (id == null)
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        long longId;
        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        Optional<Post> postOptional = postRepository.findById(longId);
        if (postOptional.isEmpty())
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(postOptional.get(), HttpStatus.OK);
    }
}
