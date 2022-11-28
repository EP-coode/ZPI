package com.core.backend.service;

import com.core.backend.exception.NoCommentException;
import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.WrongIdException;

public interface RatingService {
    enum LikeOrDislikeResult{
        LIKE_OR_DISLIKE_DELETED,
        LIKE_OR_DISLIKE_CHANGED,
        LIKE_OR_DISLIKE_CREATED
    }
    LikeOrDislikeResult createPostLikeOrDislike(String postId, String email, boolean likes) throws NoIdException, NoPostException, WrongIdException;
    LikeOrDislikeResult createCommentLikeOrDislike(String commentId, String email, boolean likes) throws NoIdException, NoCommentException, WrongIdException;
}
