package com.core.backend.model;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;
    @ManyToOne
    @JoinColumn(name = "creator_id_fk")
    private User creator;
    private int totalLikes;
    private int totalDislikes;
    private String content;
    private Date creationTime;

    public Comment(Post post, User creator, String content) {
        this.post = post;
        this.creator = creator;
        this.content = content;
        this.creationTime = new Date(System.currentTimeMillis());
        this.totalLikes = 0;
        this.totalDislikes = 0;
    }

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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

    public void addLikeOrDislike(boolean likes){
        if(likes) this.totalLikes += 1; else this.totalDislikes += 1;
    }

    public void deleteLikeOrDislike(boolean likes){
        if(likes) this.totalLikes -= 1; else this.totalDislikes -= 1;
    }

    public void changeLikeOrDislike(boolean likes){
        if(likes){
            this.totalLikes += 1;
            this.totalDislikes -= 1;
        }else{
            this.totalLikes -= 1;
            this.totalDislikes += 1;
        }
    }
}
