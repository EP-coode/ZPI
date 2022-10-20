package com.core.backend.PostCategoryGroup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PostCategoryGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postCategoryGroupId;
    private String displayName;
    private int totalPosts;

    public long getPostCategoryGroupId() {
        return postCategoryGroupId;
    }

    public void setPostCategoryGroupId(long postCategoryGroupId) {
        this.postCategoryGroupId = postCategoryGroupId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }
}
