package com.core.backend.repository;

import com.core.backend.model.FollowedUser;
import com.core.backend.id.FollowedUserId;
import com.core.backend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FollowedUserRepository extends CrudRepository<FollowedUser, FollowedUserId> {
    List<FollowedUser> findAll();

    List<FollowedUser> findAllByPrimaryKey_UserId(User userId);

    List<FollowedUser> findAllByPrimaryKey_FollowedUserId(User followedUserId);
}
