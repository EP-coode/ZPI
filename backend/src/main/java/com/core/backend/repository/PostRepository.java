package com.core.backend.repository;

import com.core.backend.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
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
    @Query("SELECT p FROM Post p " +
            "WHERE (:categoryGroupId is NULL OR p.category.postCategoryGroup.displayName LIKE :categoryGroupId) " +
            "AND (:category is NULL OR p.category.displayName LIKE :category) " +
            "AND (:creatorId is NULL OR p.creator.userId = :creatorId) " +
            "AND (:startDate is NULL OR p.creationTime BETWEEN :startDate AND :endDate)")
    List<Post> findPostsFiltered(String categoryGroupId, String category, Long creatorId, Date startDate, Date endDate, Sort sort);
}
