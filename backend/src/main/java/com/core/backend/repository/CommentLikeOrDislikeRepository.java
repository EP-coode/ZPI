package com.core.backend.repository;

import com.core.backend.model.CommentLikeOrDislike;
import com.core.backend.id.CommentLikeOrDislikeId;
import org.springframework.data.repository.CrudRepository;

public interface CommentLikeOrDislikeRepository extends CrudRepository<CommentLikeOrDislike, CommentLikeOrDislikeId> {
}
