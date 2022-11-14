package com.core.backend.dto.mapper;

import com.core.backend.dto.post.PostCreateUpdateDto;
import com.core.backend.dto.post.PostDto;
import com.core.backend.model.Post;
import com.core.backend.model.User;

public class PostMapper {

    public static PostDto toPostDto(Post post) {
        return new PostDto(post.getPostId(), post.getCreator(), post.getApprover().getUserId(),
                    post.getCategory(), post.getTitle(), post.getImageUrl(), post.getTotalLikes(),
                    post.getTotalDislikes(), post.getApproveTime(), post.getCreationTime(), post.getMarkdownContent());
    }

//    public static Post toPost(PostDto postDto) throws Exception {
//        return new Post(postDto.getCreator(), null, postDto.getCategory(), postDto.getTitle(),
//                postDto.getImageUrl(), postDto.getMarkdownContent());
//    }

    public static Post toPost(PostCreateUpdateDto postCreateDto, User creator) {
        return new Post(creator, creator, postCreateDto.getCategory(), postCreateDto.getTitle(),
                postCreateDto.getImageUrl(), postCreateDto.getMarkdownContent());
    }

}
