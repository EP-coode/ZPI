package com.core.backend.repository;

import com.core.backend.model.PostCategory;
import org.springframework.data.repository.CrudRepository;

public interface PostCategoryRepository extends CrudRepository<PostCategory, Long> {
    PostCategory findByDisplayName(String displayName);
}
