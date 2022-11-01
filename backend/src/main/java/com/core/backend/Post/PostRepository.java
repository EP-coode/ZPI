package com.core.backend.Post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
//    List<Post> findAll();
    @Query("Select p From Post p")
    List<Post> findAllPosts(Pageable page);
}
