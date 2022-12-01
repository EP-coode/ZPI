package com.core.backend.service;

import com.core.backend.dto.likeOrDislike.LikeOrDislikeResponse;
import com.core.backend.exception.NoCommentException;
import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.WrongIdException;

public interface RatingService {

    LikeOrDislikeResponse createPostLikeOrDislike(String postId, String email, boolean likes) throws NoIdException, NoPostException, WrongIdException;
    LikeOrDislikeResponse createCommentLikeOrDislike(String commentId, String email, boolean likes) throws NoIdException, NoCommentException, WrongIdException;
}
