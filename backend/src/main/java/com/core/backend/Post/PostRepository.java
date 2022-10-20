package com.core.backend.Post;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
//    List<Post> findAll();
}
