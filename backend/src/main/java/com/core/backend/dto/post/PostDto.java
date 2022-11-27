package com.core.backend.dto.post;

import com.core.backend.dto.user.UserDto;
import com.core.backend.model.PostCategory;
import com.core.backend.model.PostTag;
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
    private UserDto creator;
    private long approverId;
    private PostCategory category;
    private String title;
    private String imageUrl;
    private int totalLikes;
    private int totalDislikes;
    private Date approveTime;
    private Date creationTime;
    private String markdownContent;
    private Boolean isLiked;
    private Set<PostTag> postTags;
    private UserDto author;

    public PostDto(long postId, UserDto creator, long approverId, PostCategory category, String title, String imageUrl,
            int totalLikes, int totalDislikes, Date approveTime, Date creationTime, String markdownContent,
            Set<PostTag> postTags, UserDto author) {
        this.postId = postId;
        this.creator = creator;
        this.approverId = approverId;
        this.category = category;
        this.title = title;
        this.imageUrl = imageUrl;
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
        this.approveTime = approveTime;
        this.creationTime = creationTime;
        this.markdownContent = markdownContent;
        this.postTags = postTags;
        this.author = author;
    }
}
