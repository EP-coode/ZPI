package com.core.backend.dto.mapper;

import com.core.backend.dto.post.PostCreateUpdateDto;
import com.core.backend.dto.post.PostDto;
import com.core.backend.model.Post;
import com.core.backend.model.PostCategory;
import com.core.backend.model.PostTag;
import com.core.backend.model.User;

import java.util.Set;

public class PostMapper {

    private static final String imageUriPrefix = "https://studentcommunityimages.blob.core.windows.net/images/";

    public static PostDto toPostDto(Post post, Boolean isLiked) {
        return new PostDto(post.getPostId(), UserMapper.toUserDto(post.getCreator()), post.getApprover().getUserId(),
                post.getCategory(),
                post.getTitle(), post.getImageUrl() != null ? imageUriPrefix + post.getImageUrl() : null,
                post.getTotalLikes(), post.getTotalDislikes(),
                post.getApproveTime(), post.getCreationTime(), post.getMarkdownContent(), isLiked, post.getPostTags(),
                UserMapper.toUserDto(post.getCreator()));
    }

    public static PostDto toPostDto(Post post) {
        return new PostDto(post.getPostId(),
                post.getCreator() != null ? UserMapper.toUserDto(post.getCreator()) : null,
                post.getApprover() != null ? post.getApprover().getUserId() : null,
                post.getCategory(),
                post.getTitle(),
                post.getImageUrl(),
                post.getTotalLikes(),
                post.getTotalDislikes(),
                post.getApproveTime(),
                post.getCreationTime(),
                post.getMarkdownContent(),
                post.getPostTags(),
                post.getCreator() != null ? UserMapper.toUserDto(post.getCreator()) : null
                );
    }

    // public static Post toPost(PostDto postDto) throws Exception {
    // return new Post(postDto.getCreator(), null, postDto.getCategory(),
    // postDto.getTitle(),
    // postDto.getImageUrl(), postDto.getMarkdownContent());
    // }

    public static Post toPost(PostCreateUpdateDto postCreateDto, User creator, PostCategory postCategory,
            Set<PostTag> postTags) {
        return new Post(creator, creator, postCategory, postCreateDto.getTitle(),
                null, postCreateDto.getMarkdownContent(), postTags);
    }

}
