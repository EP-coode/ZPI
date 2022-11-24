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
    private String categoryName;
    private String title;
    private String markdownContent;
    private Set<String> tagNames;
}

