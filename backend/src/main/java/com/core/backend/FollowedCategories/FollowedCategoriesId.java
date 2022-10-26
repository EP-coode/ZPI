package com.core.backend.FollowedCategories;

import com.core.backend.PostCategory.PostCategory;
import com.core.backend.User.User;

import javax.persistence.Embeddable;
import javax.persistence.Id;
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
