package com.core.backend.model.id;

import com.core.backend.model.Comment;
import com.core.backend.model.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class CommentLikeOrDislikeId implements Serializable {
    @ManyToOne
    private Comment commentId;
    @ManyToOne
    private User userId;

    public CommentLikeOrDislikeId(Comment commentId, User userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

    public CommentLikeOrDislikeId() {
    }

    public Comment getCommentId() {
        return commentId;
    }

    public void setCommentId(Comment commentId) {
        this.commentId = commentId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
