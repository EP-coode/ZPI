package com.core.backend.Comment;

import com.core.backend.Post.Post;
import com.core.backend.User.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long commentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "creator_id_fk")
    private User creatorId;
    private int totalLikes;
    private int totalDislikes;
    private String content;
    private Date creationTime;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post postId) {
        this.post = postId;
    }

    public User getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(User creatorId) {
        this.creatorId = creatorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
