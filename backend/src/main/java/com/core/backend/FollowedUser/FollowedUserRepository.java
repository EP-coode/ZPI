package com.core.backend.FollowedUser;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FollowedUserRepository extends CrudRepository<FollowedUser, FollowedUserId> {
    List<FollowedUser> findAll();
}
