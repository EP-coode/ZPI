package com.core.backend.Post;

import com.core.backend.Comment.Comment;
import com.core.backend.Comment.CommentRepository;
import com.core.backend.utilis.NoIdException;
import com.core.backend.utilis.Utilis;
import com.core.backend.utilis.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private Utilis utilis;

    @GetMapping
    public ResponseEntity<Object> getPosts(int page, Sort.Direction sort, int pageSize) {
        return new ResponseEntity<>(postRepository
                .findAllPosts(PageRequest.of(page, pageSize, Sort.by(sort, "postId"))), HttpStatus.OK);
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
    public ResponseEntity<Object> getComments(@PathVariable String postId, int page, Sort.Direction sort, int pageSize) {
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
        return new ResponseEntity<>(commentRepository.findByPostId(post.getPostId()
                        , (Pageable) PageRequest.of(page, pageSize, Sort.by(sort, "commentId"))), HttpStatus.OK);
    }

    @GetMapping(value = "/{postId}/comments/{comment}")
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
        List<Comment> comments = commentRepository.findByPostId(post.getPostId());
        comments = comments.stream()
                .filter(comment -> comment.getCommentId() == commentLongId)
                .collect(Collectors.toList());
        if (comments.isEmpty()) {
            return new ResponseEntity<>("Brak komentarza o podanym ID w poście", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comments.get(0), HttpStatus.OK);
    }
}
