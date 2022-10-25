package com.core.backend.Comment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Query("Select c From Comment c where c.postId=?1")
    List<Comment> findByPostId(long postId, Pageable page);
//    List<Comment> findByPostId(long postId);
}
