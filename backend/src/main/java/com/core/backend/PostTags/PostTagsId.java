package com.core.backend.PostTags;

import com.core.backend.Post.Post;
import com.core.backend.PostTag.PostTag;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class PostTagsId implements Serializable {
    @ManyToOne
    private Post postId;
    @ManyToOne
    private PostTag tagName;

    public PostTagsId(Post postId, PostTag tagName) {
        this.postId = postId;
        this.tagName = tagName;
    }

    public PostTagsId() {
    }

    public Post getPostId() {
        return postId;
    }

    public void setPostId(Post postId) {
        this.postId = postId;
    }

    public PostTag getTagName() {
        return tagName;
    }

    public void setTagName(PostTag tagName) {
        this.tagName = tagName;
    }
}
