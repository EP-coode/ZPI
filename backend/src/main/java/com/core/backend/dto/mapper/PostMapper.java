package com.core.backend.dto.mapper;

import com.core.backend.dto.post.PostCreateUpdateDto;
import com.core.backend.dto.post.PostDto;
import com.core.backend.model.Post;
import com.core.backend.model.PostCategory;
import com.core.backend.model.PostTag;
import com.core.backend.model.User;

import java.util.HashSet;
import java.util.Set;

public class PostMapper {

    public static PostDto toPostDto(Post post, Boolean isLiked) {
        return new PostDto(post.getPostId(), UserMapper.toUserDto(post.getCreator()), post.getApprover().getUserId(), post.getCategory(),
                    post.getTitle(), post.getImageUrl(), post.getTotalLikes(), post.getTotalDislikes(),
                    post.getApproveTime(), post.getCreationTime(), post.getMarkdownContent(), isLiked, post.getPostTags());
    }

    public static PostDto toPostDto(Post post) {
        return new PostDto(post.getPostId(), UserMapper.toUserDto(post.getCreator()), post.getApprover().getUserId(), post.getCategory(),
                post.getTitle(), post.getImageUrl(), post.getTotalLikes(), post.getTotalDislikes(),
                post.getApproveTime(), post.getCreationTime(), post.getMarkdownContent(),post.getPostTags());
    }

//    public static Post toPost(PostDto postDto) throws Exception {
//        return new Post(postDto.getCreator(), null, postDto.getCategory(), postDto.getTitle(),
//                postDto.getImageUrl(), postDto.getMarkdownContent());
//    }

    public static Post toPost(PostCreateUpdateDto postCreateDto, User creator, PostCategory postCategory, Set<PostTag> postTags) {
        return new Post(creator, creator, postCategory, postCreateDto.getTitle(),
                null, postCreateDto.getMarkdownContent(), postTags);
    }

}
