package com.core.backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class PostCategoryGroup {

    @Id
    private String displayName;
    private int totalPosts;
    @JsonIgnoreProperties("postCategoryGroup")
    @OneToMany(mappedBy="postCategoryGroup")
    private List<PostCategory> postCategories = new ArrayList<>();

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

    public List<PostCategory> getPostCategories() {
        return postCategories;
    }

    
}
