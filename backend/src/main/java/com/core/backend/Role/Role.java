package com.core.backend.Role;

import javax.persistence.*;

@Entity
public class Role {
    @Id
    private String roleName;
    private boolean canCreatePost;
    private boolean canApprovePost;
    private boolean canReadPost;
    private int postPerDayLimit;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isCanCreatePost() {
        return canCreatePost;
    }

    public void setCanCreatePost(boolean canCreatePost) {
        this.canCreatePost = canCreatePost;
    }

    public boolean isCanApprovePost() {
        return canApprovePost;
    }

    public boolean isCanReadPost() {
        return canReadPost;
    }

    public void setCanReadPost(boolean canReadPost) {
        this.canReadPost = canReadPost;
    }

    public void setCanApprovePost(boolean canApprovePost) {
        this.canApprovePost = canApprovePost;
    }

    public int getPostPerDayLimit() {
        return postPerDayLimit;
    }

    public void setPostPerDayLimit(int postPerDayLimit) {
        this.postPerDayLimit = postPerDayLimit;
    }
}
