package com.core.backend.model;

import com.core.backend.model.id.PostLikeOrDislikeId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class PostLikeOrDislike {

    @EmbeddedId
    private PostLikeOrDislikeId postLikeOrDislikeId;
    private boolean likes;

    public PostLikeOrDislikeId getPrimaryKey() {
        return postLikeOrDislikeId;
    }

    public void setPostLikeOrDislikeId(PostLikeOrDislikeId postLikeOrDislikeId) {
        this.postLikeOrDislikeId = postLikeOrDislikeId;
    }

    public PostLikeOrDislikeId getPostLikeOrDislikeId() {
        return postLikeOrDislikeId;
    }

    public boolean isLikes() {
        return likes;
    }

    public void setLikes(boolean likes) {
        this.likes = likes;
    }
}
