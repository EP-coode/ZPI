package com.core.backend.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile photo;
}

