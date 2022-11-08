package com.core.backend.dto.mapper;

import com.core.backend.dto.CommentDto;
import com.core.backend.model.Comment;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getCommentId(), comment.getPost().getPostId(),
                comment.getCreator(), comment.getTotalLikes(), comment.getTotalDislikes(),
                comment.getContent(), comment.getCreationTime());
    }
}
