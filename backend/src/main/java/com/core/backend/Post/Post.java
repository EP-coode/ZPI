package com.core.backend.Post;

import com.core.backend.PostCategory.PostCategory;
import com.core.backend.User.User;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;
    @ManyToOne
    @JoinColumn(name = "creator_id_fk")
    private User creatorId;
    @ManyToOne
    @JoinColumn(name = "approver_id_fk")
    private User approverId;
    @ManyToOne
    @JoinColumn(name = "post_category_id_fk")
    private PostCategory categoryId;
    private String title;
    private String imageUrl;
    private int totalLikes;
    private int totalDislikes;
    private Date approveTime;
    private Date creationTime;

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public User getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(User creatorId) {
        this.creatorId = creatorId;
    }

    public User getApproverId() {
        return approverId;
    }

    public void setApproverId(User approverId) {
        this.approverId = approverId;
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
}
