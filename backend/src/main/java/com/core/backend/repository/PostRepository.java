package com.core.backend.repository;

import com.core.backend.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findAll();
    @Query("Select p From Post p")
    List<Post> findAllPosts();
    @Query("Select p From Post p")
    List<Post> findAllPosts(Pageable page);
    @Query("SELECT p FROM Post p WHERE p.category.displayName LIKE %?1%")
    List<Post> findAllPostsByCategoryName(String name);
    List<Post> findPostsByPostTagsTagName(String postTagName);
}
