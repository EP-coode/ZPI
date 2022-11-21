package com.core.backend.dto.post;

import com.core.backend.model.PostCategory;
import com.core.backend.model.PostTag;
import lombok.*;

import java.util.List;
import java.util.Set;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateUpdateDto {
    private PostCategory category;
    private String title;
    private String imageUrl;
    private String markdownContent;
    private Set<String> tags;
}

