package com.core.backend.model;

import com.core.backend.id.FollowedCategoriesId;

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
