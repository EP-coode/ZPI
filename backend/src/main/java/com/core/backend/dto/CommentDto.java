package com.core.backend.dto;

import com.core.backend.model.User;
import lombok.*;

import java.util.Date;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long commentId;
    private long postId;
    private User creator;
    private int totalLikes;
    private int totalDislikes;
    private String content;
    private Date creationTime;
}
