package com.core.backend.dto.post;

import com.core.backend.model.PostCategory;
import com.core.backend.model.PostTag;
import com.core.backend.model.User;
import lombok.*;

import java.sql.Date;
import java.util.Set;

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
    private Set<PostTag> postTags;
}
