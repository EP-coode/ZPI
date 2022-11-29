package com.core.backend.dto.likeOrDislike;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeOrDislikeResponse {
    private int totalLikes;
    private String message;
    private Boolean isLiked;
}
