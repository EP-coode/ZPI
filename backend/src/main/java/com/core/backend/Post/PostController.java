package com.core.backend.Post;

import com.core.backend.Comment.Comment;
import com.core.backend.Comment.CommentRepository;
import com.core.backend.utilis.NoIdException;
import com.core.backend.utilis.Utilis;
import com.core.backend.utilis.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private Utilis utilis;


    public static final int PAGE_SIZE = 5;

    @GetMapping
    public ResponseEntity<Object> getPosts(@RequestParam(required = false) Integer page, Sort.Direction sort) {
        page = page == null ? 0 : page;
        Pageable pageableRequest = PageRequest.of(page, PAGE_SIZE, sort, "postId");
        return new ResponseEntity<>(postRepository
                .findAllPosts(pageableRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<Object> getPost(@PathVariable String postId) {
        long longId;
        try {
            longId = utilis.convertId(postId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        Optional<Post> postOptional = postRepository.findById(longId);
        if (postOptional.isEmpty())
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(postOptional.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/{postId}/comments")
    public ResponseEntity<Object> getComments(@PathVariable String postId, @RequestParam(required = false) Integer page, Sort.Direction sort) {
        long longId;
        try {
            longId = utilis.convertId(postId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        Optional<Post> postOptional = postRepository.findById(longId);
        if (postOptional.isEmpty())
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);
        Post post = postOptional.get();
        page = page == null ? 0 : page;
        Pageable pageableRequest = PageRequest.of(page, PAGE_SIZE, sort, "commentId");
        return new ResponseEntity<>(commentRepository.findAllCommentsByPostId(post.getPostId()
                        , pageableRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/{postId}/comments/{commentId}")
    public ResponseEntity<Object> getComment(@PathVariable String postId, @PathVariable String commentId) {
        long postLongId, commentLongId;
        try {
            postLongId = utilis.convertId(postId);
            commentLongId = utilis.convertId(commentId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }

        Optional<Post> postOptional = postRepository.findById(postLongId);
        if (postOptional.isEmpty())
            return new ResponseEntity<>("Brak użytkownika o podanym ID", HttpStatus.NOT_FOUND);

        Post post = postOptional.get();
        Long postIdRes = post.getPostId();
        List<Comment> comments = commentRepository.findAllCommentsByPostId(postIdRes);
        comments = comments.stream()
                .filter(comment -> comment.getCommentId() == commentLongId)
                .collect(Collectors.toList());
        if (comments.isEmpty()) {
            return new ResponseEntity<>("Brak komentarza o podanym ID w poście", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comments.get(0), HttpStatus.OK);
    }
}
