package com.core.backend.repository;

import com.core.backend.model.CommentLikeOrDislike;
import com.core.backend.id.CommentLikeOrDislikeId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentLikeOrDislikeRepository extends CrudRepository<CommentLikeOrDislike, CommentLikeOrDislikeId> {
    @Modifying
    @Query("DELETE FROM CommentLikeOrDislike c WHERE c.commentLikeOrDislikeId.commentId.commentId = ?1")
    void deleteAllByCommentId(Long commentId);
}
