package com.core.backend.Comment;

import com.core.backend.Post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.post.postId = ?1")
    List<Comment> findAllCommentsByPostId(Long post, Pageable page);
    @Query("SELECT c FROM Comment c WHERE c.post.postId = ?1")
    List<Comment> findAllCommentsByPostId(Long post);
}
