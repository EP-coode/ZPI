package com.core.backend.dto.comment;

import java.util.List;

public class CommentWithPaginationDto {
    public List<CommentDto> comments;
    public long totalComments;

    public CommentWithPaginationDto(List<CommentDto> comments, long totalComments) {
        this.comments = comments;
        this.totalComments = totalComments;
    }
}
