package com.core.backend.dto;

import com.core.backend.model.PostCategory;
import com.core.backend.model.User;
import lombok.*;

import java.sql.Date;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private long postId;
    private User creator;
    private long approverId;
    private PostCategory category;
    private String title;
    private String imageUrl;
    private int totalLikes;
    private int totalDislikes;
    private Date approveTime;
    private Date creationTime;
    private String markdownContent;
}
