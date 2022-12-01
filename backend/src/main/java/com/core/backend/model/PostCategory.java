package com.core.backend.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class PostCategory {

    @Id
    private String displayName;
    @JsonIgnoreProperties("postCategories")
    @ManyToOne
    @JoinColumn(name="post_category_group_id_fk")
    private PostCategoryGroup postCategoryGroup;
    private int totalPosts;

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
