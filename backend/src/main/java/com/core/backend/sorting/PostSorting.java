package com.core.backend.sorting;

import com.core.backend.dto.filter.PostOrdering;
import org.springframework.data.domain.Sort;

public class PostSorting {

    public static Sort getSortingByPostOrdering(PostOrdering orderBy) {
        switch (orderBy) {
            case DATE_ASC -> {
                return Sort.by("creationTime").ascending();
            }
            case DATE_DSC -> {
                return Sort.by("creationTime").descending();
            }
            case LIKES_ASC -> {
                return Sort.by("totalLikes").ascending();
            }
            case LIKES_DSC -> {
                return Sort.by("totalLikes").descending();
            }
            default -> {
                return Sort.unsorted();
            }
        }
    }
}
