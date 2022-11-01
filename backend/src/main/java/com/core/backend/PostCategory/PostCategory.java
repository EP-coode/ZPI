package com.core.backend.PostCategory;

import com.core.backend.PostCategoryGroup.PostCategoryGroup;

import javax.persistence.*;

@Entity
public class PostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postCategoryId;
    @ManyToOne
    @JoinColumn
    private PostCategoryGroup postCategoryGroupId;
    private String displayName;
    private int totalPosts;

    public long getPostCategoryId() {
        return postCategoryId;
    }

    public void setPostCategoryId(long postCategoryId) {
        this.postCategoryId = postCategoryId;
    }

    public PostCategoryGroup getPostCategoryGroupId() {
        return postCategoryGroupId;
    }

    public void setPostCategoryGroupId(PostCategoryGroup postCategoryGroupId) {
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
