package com.core.backend.model;

import com.core.backend.model.id.CommentLikeOrDislikeId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CommentLikeOrDislike {
    @EmbeddedId
    private CommentLikeOrDislikeId commentLikeOrDislikeId;
    private boolean likes;

    public CommentLikeOrDislikeId getPrimaryKey() {
        return commentLikeOrDislikeId;
    }

    public CommentLikeOrDislikeId getCommentLikeOrDislikeId() {
        return commentLikeOrDislikeId;
    }

    public void setCommentLikeOrDislikeId(CommentLikeOrDislikeId commentLikeOrDislikeId) {
        this.commentLikeOrDislikeId = commentLikeOrDislikeId;
    }

    public boolean isLikes() {
        return likes;
    }

    public void setLikes(boolean likes) {
        this.likes = likes;
    }
}
