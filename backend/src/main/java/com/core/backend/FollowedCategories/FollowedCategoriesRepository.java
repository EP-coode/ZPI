package com.core.backend.FollowedCategories;

import com.core.backend.FollowedUser.FollowedUser;
import com.core.backend.User.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FollowedCategoriesRepository extends CrudRepository<FollowedCategories, FollowedCategoriesId> {
    List<FollowedCategories> findAll();
}