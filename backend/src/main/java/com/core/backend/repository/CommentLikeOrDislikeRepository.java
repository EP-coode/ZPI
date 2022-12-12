package com.core.backend.repository;

import com.core.backend.model.CommentLikeOrDislike;
import com.core.backend.model.id.CommentLikeOrDislikeId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommentLikeOrDislikeRepository extends CrudRepository<CommentLikeOrDislike, CommentLikeOrDislikeId> {
    @Modifying
    @Query("DELETE FROM CommentLikeOrDislike c WHERE c.commentLikeOrDislikeId.commentId.commentId = ?1")
    void deleteAllByCommentId(Long commentId);
}
