package com.core.backend.repository;

import com.core.backend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.post.postId = ?1")
    Page<Comment> findAllCommentsByPostId(Long post, Pageable page);
    @Query("SELECT c FROM Comment c WHERE c.post.postId = ?1")
    List<Comment> findAllCommentsByPostId(Long post);
}
