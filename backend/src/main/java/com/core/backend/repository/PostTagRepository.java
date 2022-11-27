package com.core.backend.repository;

import com.core.backend.model.PostTag;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostTagRepository extends CrudRepository<PostTag, String> {
    List<PostTag> findPostTagsByPostsPostId(Long postId);
    @Query("SELECT pt FROM PostTag pt WHERE pt.tagName LIKE ?1% ORDER BY pt.totalPosts DESC")
    List<PostTag> getTagsByPrefix(String prefix, Pageable pageable);
}
