package com.core.backend.model;

import javax.persistence.*;

@Entity
public class PostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postCategoryId;
    @ManyToOne
    @JoinColumn(name="post_category_group_id_fk")
    private PostCategoryGroup postCategoryGroup;
    private String displayName;
    private int totalPosts;

    public long getPostCategoryId() {
        return postCategoryId;
    }

    public void setPostCategoryId(long postCategoryId) {
        this.postCategoryId = postCategoryId;
    }

    public PostCategoryGroup getPostCategoryGroup() {
        return postCategoryGroup;
    }

    public void setPostCategoryGroup(PostCategoryGroup postCategoryGroup) {
        this.postCategoryGroup = postCategoryGroup;
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
