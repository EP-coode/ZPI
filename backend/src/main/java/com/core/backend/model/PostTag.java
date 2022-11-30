package com.core.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PostTag {
    @Id
    private String tagName;
    private int totalPosts;
    @ManyToMany(mappedBy = "postTags")
    @JsonIgnore
    private Set<Post> posts;

    public PostTag(String tagName) {
        this.tagName = tagName;
        this.totalPosts = 1;
        posts = new HashSet<>();
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostTag postTag = (PostTag) o;
        return tagName.equals(postTag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName);
    }
}
