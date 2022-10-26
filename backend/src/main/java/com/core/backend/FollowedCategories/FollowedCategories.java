package com.core.backend.FollowedCategories;

import com.core.backend.FollowedUser.FollowedUserId;

import javax.persistence.*;

@Entity
public class FollowedCategories {

    @EmbeddedId
    private FollowedCategoriesId primaryKey;

    public FollowedCategoriesId getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(FollowedCategoriesId primaryKey) {
        this.primaryKey = primaryKey;
    }
}
