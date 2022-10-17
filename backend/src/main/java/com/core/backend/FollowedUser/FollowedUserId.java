package com.core.backend.FollowedUser;

import com.core.backend.User.User;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;


// https://stackoverflow.com/questions/32625410/hibernate-foreign-key-with-a-part-of-composite-primary-key

@Embeddable
public class FollowedUserId implements Serializable {
    @ManyToOne
//    @JoinColumn(name = "user_id")
    private User userId;
    @ManyToOne
//    @JoinColumn(name = "user_id")
    private User followedUserId;

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public User getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(User followedUserId) {
        this.followedUserId = followedUserId;
    }
}
