package com.core.backend.comparator;

import com.core.backend.dto.filter.PostOrdering;
import com.core.backend.model.Post;

import java.util.Comparator;

public class PostComparators {
    private static final Comparator<Post> dateAscComparator = new Comparator<Post>() {

        @Override
        public int compare(Post p1, Post p2) {
            return (int) (p1.getCreationTime().getTime() - p2.getCreationTime().getTime());
        }
    };

    private static final Comparator<Post> dateDscComparator = new Comparator<Post>() {

        @Override
        public int compare(Post p1, Post p2) {
            return (int) (p2.getCreationTime().getTime() - p1.getCreationTime().getTime());
        }
    };

    private static final Comparator<Post> likesAscComparator = new Comparator<Post>() {

        @Override
        public int compare(Post p1, Post p2) {
            return p1.getTotalLikes() - p2.getTotalLikes();
        }
    };

    private static final Comparator<Post> likesDscComparator = new Comparator<Post>() {

        @Override
        public int compare(Post p1, Post p2) {
            return p2.getTotalLikes() - p1.getTotalLikes();
        }
    };

    public static Comparator<Post> getComparator(PostOrdering orderBy) {
        switch (orderBy) {
            case DATE_ASC -> {
                return dateAscComparator;
            }
            case DATE_DSC -> {
                return dateDscComparator;
            }
            case LIKES_ASC -> {
                return likesAscComparator;
            }
            case LIKES_DSC -> {
                return likesDscComparator;
            }
        }
        return null;
    }
}
