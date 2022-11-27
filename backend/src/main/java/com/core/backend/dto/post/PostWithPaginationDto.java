package com.core.backend.dto.post;

import java.util.List;

public class PostWithPaginationDto {
    public List<PostDto> posts;
    public long totalPosts;

    public PostWithPaginationDto(List<PostDto> posts, long totapPosts) {
        this.posts = posts;
        this.totalPosts = totapPosts;
    }
}
