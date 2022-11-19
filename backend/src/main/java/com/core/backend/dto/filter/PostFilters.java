package com.core.backend.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostFilters {
    private String[] tagNames;
    private String categoryGroup;
    private String category;
    private Long creatorId;
}
