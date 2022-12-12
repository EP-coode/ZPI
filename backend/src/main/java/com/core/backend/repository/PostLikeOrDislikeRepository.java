package com.core.backend.repository;

import com.core.backend.model.PostLikeOrDislike;
import com.core.backend.model.id.PostLikeOrDislikeId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostLikeOrDislikeRepository extends CrudRepository<PostLikeOrDislike, PostLikeOrDislikeId> {
    @Query("Select p From PostLikeOrDislike p WHERE p.postLikeOrDislikeId.postId.postId = ?1")
    List<PostLikeOrDislike> findAllByPostId(Long postId);

}
