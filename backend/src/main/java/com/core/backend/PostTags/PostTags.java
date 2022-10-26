package com.core.backend.PostTags;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class PostTags {
    @EmbeddedId
    private PostTagsId postTagsId;

    public PostTagsId getPostTagsId() {
        return postTagsId;
    }

    public void setPostTagsId(PostTagsId postTagsId) {
        this.postTagsId = postTagsId;
    }

    public PostTagsId getPrimaryKey() {
        return postTagsId;
    }
}
