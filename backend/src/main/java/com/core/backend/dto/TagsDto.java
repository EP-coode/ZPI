package com.core.backend.dto;

import java.util.List;

import com.core.backend.model.PostTag;

public class TagsDto {
    public List<PostTag> tags;

    public TagsDto(List<PostTag> tags) {
        this.tags = tags;
    }
    
}
