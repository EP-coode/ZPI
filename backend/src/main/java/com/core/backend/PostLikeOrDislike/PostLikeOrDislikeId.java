package com.core.backend.PostLikeOrDislike;

import com.core.backend.Post.Post;
import com.core.backend.User.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class PostLikeOrDislikeId implements Serializable {

    @ManyToOne
    private User userId;
    @ManyToOne
    private Post postId;

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
