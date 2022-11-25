package com.core.backend.service;

import com.core.backend.exception.NoCommentException;
import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.WrongIdException;
import com.core.backend.id.CommentLikeOrDislikeId;
import com.core.backend.id.PostLikeOrDislikeId;
import com.core.backend.model.*;
import com.core.backend.repository.*;
import com.core.backend.utilis.Utilis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeOrDislikeRepository postLikeOrDislikeRepository;

    @Autowired
    private CommentLikeOrDislikeRepository commentLikeOrDislikeRepository;

    @Autowired
    private Utilis utilis;

    @Override
    public LikeOrDislikeResult createPostLikeOrDislike(String postId, String email, boolean likes) throws NoIdException, NoPostException, WrongIdException {
        User user = userRepository.findByEmail(email);
        long longId = utilis.convertId(postId);
        Optional<Post> post = postRepository.findById(longId);
        if (post.isEmpty()) throw new NoPostException();
        PostLikeOrDislikeId postLikeOrDislikeId = new PostLikeOrDislikeId(user, post.get());
        Optional<PostLikeOrDislike> postLikeOrDislike = postLikeOrDislikeRepository.findById(postLikeOrDislikeId);
        if(postLikeOrDislike.isPresent()){
            // anulowanie oceny
            if(postLikeOrDislike.get().isLikes() == likes){
                postLikeOrDislikeRepository.delete(postLikeOrDislike.get());
                post.get().deleteLikeOrDislike(likes);
                postRepository.save(post.get());
                return LikeOrDislikeResult.LIKE_OR_DISLIKE_DELETED;
            }
            // zmiana oceny
            else{
                postLikeOrDislike.get().setLikes(likes);
                postLikeOrDislikeRepository.save(postLikeOrDislike.get());
                post.get().changeLikeOrDislike(likes);
                postRepository.save(post.get());
                return LikeOrDislikeResult.LIKE_OR_DISLIKE_CHANGED;
            }
        }
        // utworzenie nowej oceny
        else{
            PostLikeOrDislike newPostLikeOrDislike = new PostLikeOrDislike();
            newPostLikeOrDislike.setPostLikeOrDislikeId(postLikeOrDislikeId);
            newPostLikeOrDislike.setLikes(likes);
            post.get().addLikeOrDislike(likes);
            postRepository.save(post.get());
            postLikeOrDislikeRepository.save(newPostLikeOrDislike);
            return LikeOrDislikeResult.LIKE_OR_DISLIKE_CREATED;
        }
    }

    @Override
    public LikeOrDislikeResult createCommentLikeOrDislike(String commentId, String email, boolean likes) throws NoIdException, NoCommentException, WrongIdException {
        User user = userRepository.findByEmail(email);
        long longId = utilis.convertId(commentId);
        Optional<Comment> comment = commentRepository.findById(longId);
        if (comment.isEmpty()) throw new NoCommentException();
        CommentLikeOrDislikeId commentLikeOrDislikeId = new CommentLikeOrDislikeId(comment.get(), user);
        Optional<CommentLikeOrDislike> commentLikeOrDislike = commentLikeOrDislikeRepository.findById(commentLikeOrDislikeId);
        if(commentLikeOrDislike.isPresent()){
            // anulowanie oceny
            if(commentLikeOrDislike.get().isLikes() == likes){
                commentLikeOrDislikeRepository.delete(commentLikeOrDislike.get());
                comment.get().deleteLikeOrDislike(likes);
                commentRepository.save(comment.get());
                return LikeOrDislikeResult.LIKE_OR_DISLIKE_DELETED;
            }
            // zmiana oceny
            else{
                commentLikeOrDislike.get().setLikes(likes);
                commentLikeOrDislikeRepository.save(commentLikeOrDislike.get());
                comment.get().changeLikeOrDislike(likes);
                commentRepository.save(comment.get());
                return LikeOrDislikeResult.LIKE_OR_DISLIKE_CHANGED;
            }
        }
        // utworzenie nowej oceny
        else{
            CommentLikeOrDislike newCommentLikeOrDislike = new CommentLikeOrDislike();
            newCommentLikeOrDislike.setCommentLikeOrDislikeId(commentLikeOrDislikeId);
            newCommentLikeOrDislike.setLikes(likes);
            comment.get().addLikeOrDislike(likes);
            commentRepository.save(comment.get());
            commentLikeOrDislikeRepository.save(newCommentLikeOrDislike);
            return LikeOrDislikeResult.LIKE_OR_DISLIKE_CREATED;
        }
    }
}
