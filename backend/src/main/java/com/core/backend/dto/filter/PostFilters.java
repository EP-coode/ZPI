package com.core.backend.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostFilters {
    private String[] tagNames;
    private String categoryGroupId;
    private String categoryId;
    private Long creatorId;
    private Integer maxPostDaysAge;
    private PostOrdering orderBy;
}
