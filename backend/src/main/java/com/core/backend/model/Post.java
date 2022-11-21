package com.core.backend.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;
    @ManyToOne
    @JoinColumn(name = "creator_id_fk")
    private User creator;
    @ManyToOne
    @JoinColumn(name = "approver_id_fk")
    private User approver;
    @ManyToOne
    @JoinColumn(name = "post_category_id_fk")
    private PostCategory category;
    @ManyToMany
    @JoinTable(
        name = "post_post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_name")
    )
    Set<PostTag> postTags;
    private String title;
    private String imageUrl;
    private int totalLikes;
    private int totalDislikes;
    private Date approveTime;
    private Date creationTime;
    @Column(length = 65535, columnDefinition = "TEXT")
    private String markdownContent;

    public Post(User creator, User approver, PostCategory category, String title, String imageUrl, String markdownContent, Set<PostTag> tags) {
        this.creator = creator;
        this.approver = approver;
        this.category = category;
        this.title = title;
        this.imageUrl = imageUrl;
        this.markdownContent = markdownContent;
        this.postTags = tags;
        this.totalDislikes = 0;
        this.totalLikes = 0;
        this.creationTime = new Date(System.currentTimeMillis());
        this.approveTime = new Date(System.currentTimeMillis());
    }

    public Post(PostCategory category, String title, String imageUrl, String markdownContent, Set<PostTag> tags) {
        this.category = category;
        this.title = title;
        this.imageUrl = imageUrl;
        this.markdownContent = markdownContent;
        this.postTags = tags;
        this.totalDislikes = 0;
        this.totalLikes = 0;
        this.creationTime = new Date(System.currentTimeMillis());
        this.approveTime = new Date(System.currentTimeMillis());
    }

    public Set<PostTag> getPostTags() {
        return postTags;
    }

    public void setPostTags(Set<PostTag> postTags) {
        this.postTags = postTags;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalDislikes() {
        return totalDislikes;
    }

    public void setTotalDislikes(int totalDislikes) {
        this.totalDislikes = totalDislikes;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public PostCategory getCategory() {
        return category;
    }

    public void setCategory(PostCategory category) {
        this.category = category;
    }

    public String getMarkdownContent() {
        return markdownContent;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return postId == post.postId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }

    public void addPostTag(PostTag postTag) {
        this.postTags.add(postTag);
        postTag.setTotalPosts(postTag.getTotalPosts() + 1);
        postTag.getPosts().add(this);
    }

    public void deletePostTag(PostTag postTag) {
        this.postTags.remove(postTag);
        postTag.setTotalPosts(postTag.getTotalPosts() - 1);
        postTag.getPosts().remove(this);
    }
}
