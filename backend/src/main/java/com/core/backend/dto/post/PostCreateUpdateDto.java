package com.core.backend.dto.post;

import lombok.*;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateUpdateDto {
    @NotBlank
    private String categoryName;
    @NotNull
    @Size(min = 3, max = 100)
    private String title;
    @NotNull
    @Size(min = 10, max = 10000)
    private String markdownContent;
    private Set<String> tagNames;
    private MultipartFile photo;
}

