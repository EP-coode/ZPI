package com.core.backend.repository;

import com.core.backend.model.PostTag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostTagRepository extends CrudRepository<PostTag, String> {
    List<PostTag> findPostTagsByPostsPostId(Long postId);
}
