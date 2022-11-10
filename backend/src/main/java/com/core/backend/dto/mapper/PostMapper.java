package com.core.backend.dto.mapper;

import com.core.backend.dto.PostDto;
import com.core.backend.model.Post;

public class PostMapper {
    public static PostDto toPostDto(Post post) {
        return new PostDto(post.getPostId(), post.getCreator(), post.getApprover().getUserId(),
                    post.getCategory(), post.getTitle(), post.getImageUrl(), post.getTotalLikes(),
                    post.getTotalDislikes(), post.getApproveTime(), post.getCreationTime(), post.getMarkdownContent());
    }
}
