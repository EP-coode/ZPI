package com.core.backend.repository;

import com.core.backend.model.FollowedCategories;
import com.core.backend.id.FollowedCategoriesId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FollowedCategoriesRepository extends CrudRepository<FollowedCategories, FollowedCategoriesId> {
    List<FollowedCategories> findAll();
}