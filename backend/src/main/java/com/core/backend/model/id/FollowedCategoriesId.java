package com.core.backend.model.id;

import com.core.backend.model.PostCategory;
import com.core.backend.model.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class FollowedCategoriesId implements Serializable {

    private static final long serialVersionUID = 6632748675651928624L;
    @ManyToOne
    private User user;
    @ManyToOne
    private PostCategory postCategory;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PostCategory getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(PostCategory postCategory) {
        this.postCategory = postCategory;
    }
}
