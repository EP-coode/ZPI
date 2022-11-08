package com.core.backend.model;

import com.core.backend.id.PostTagsId;

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
