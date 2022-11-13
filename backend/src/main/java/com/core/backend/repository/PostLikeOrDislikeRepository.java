package com.core.backend.repository;

import com.core.backend.model.PostLikeOrDislike;
import com.core.backend.id.PostLikeOrDislikeId;
import org.springframework.data.repository.CrudRepository;

public interface PostLikeOrDislikeRepository extends CrudRepository<PostLikeOrDislike, PostLikeOrDislikeId> {
}
