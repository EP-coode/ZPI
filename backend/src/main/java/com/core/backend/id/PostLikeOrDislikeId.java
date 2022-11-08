package com.core.backend.id;

import com.core.backend.model.Post;
import com.core.backend.model.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class PostLikeOrDislikeId implements Serializable {

    @ManyToOne
    private User userId;
    @ManyToOne
    private Post postId;

    public PostLikeOrDislikeId() {
    }

    public PostLikeOrDislikeId(User userId, Post postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Post getPostId() {
        return postId;
    }

    public void setPostId(Post postId) {
        this.postId = postId;
    }
}
