package com.core.backend.model;

import com.core.backend.id.FollowedUserId;

import javax.persistence.*;

@Entity
public class FollowedUser {
    @EmbeddedId
    private FollowedUserId primaryKey;

    public FollowedUserId getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(FollowedUserId primaryKey) {
        this.primaryKey = primaryKey;
    }
}
