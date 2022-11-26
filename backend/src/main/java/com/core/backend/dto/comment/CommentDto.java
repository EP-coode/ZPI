package com.core.backend.dto.comment;

import com.core.backend.dto.user.UserDto;
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
    private UserDto creator;
    private int totalLikes;
    private int totalDislikes;
    private String content;
    private Date creationTime;
}
