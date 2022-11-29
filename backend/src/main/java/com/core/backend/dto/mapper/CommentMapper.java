package com.core.backend.dto.mapper;

import com.core.backend.dto.comment.CommentCreateUpdateDto;
import com.core.backend.dto.comment.CommentDto;
import com.core.backend.model.Comment;
import com.core.backend.model.Post;
import com.core.backend.model.User;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getCommentId(), comment.getPost().getPostId(),
                UserMapper.toUserDto(comment.getCreator()), comment.getTotalLikes(), comment.getTotalDislikes(),
                comment.getContent(), comment.getCreationTime());
    }
    public static Comment toComment(CommentCreateUpdateDto comment, User creator, Post post) {
        return new Comment(post, creator, comment.getContent());
    }
}
