package com.core.backend.service;

import com.core.backend.dto.comment.CommentCreateUpdateDto;
import com.core.backend.dto.comment.CommentDto;
import com.core.backend.dto.comment.CommentWithPaginationDto;
import com.core.backend.dto.filter.PostFilters;
import com.core.backend.dto.post.PostCreateUpdateDto;
import com.core.backend.dto.post.PostDto;
import com.core.backend.dto.post.PostWithPaginationDto;
import com.core.backend.exception.*;
import com.core.backend.model.*;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
        List<PostDto> getAllPosts();

        List<PostDto> getAllPostsPagination(Integer page, Sort.Direction sort);

        PostWithPaginationDto getPostsFiltered(PostFilters postFilters, int page, int postPerPage);

        CommentDto getCommentById(String commentId) throws WrongIdException, NoIdException, NoCommentException;

        PostDto getPostByPostId(String postId) throws WrongIdException, NoIdException, NoPostException;

        List<CommentDto> getComments(String postId) throws WrongIdException, NoIdException, NoPostException;

        CommentWithPaginationDto getCommentsPagination(String postId, Integer page, Sort.Direction sort)
            throws WrongIdException, NoIdException, NoPostException;

        PostDto addPost(PostCreateUpdateDto postDto)
                        throws NoAccessException, NoPostCategoryException;

        void updatePost(String postId, PostCreateUpdateDto postDto)
                        throws NoAccessException, NoPostException, WrongIdException, NoIdException,
                        NoPostCategoryException;

        void deletePost(String postId) throws NoAccessException, WrongIdException, NoIdException;

        CommentDto addComment(String postId, CommentCreateUpdateDto commentCreateUpdateDto)
                        throws NoPostException, WrongIdException, NoIdException;

        void updateComment(String postId, String commentId, CommentCreateUpdateDto commentCreateUpdateDto)
                        throws NoAccessException, WrongIdException, NoIdException, NoPostException, NoCommentException;

        void deleteComment(String commentId) throws NoAccessException, WrongIdException, NoIdException;

        byte[] getPhotoByPostId(String postId) throws NoIdException, NoPostException, WrongIdException;

        PostLikeOrDislike getPostLikeOrDislike(Post post, User user);

        CommentLikeOrDislike getCommentLikeOrDislike(Comment comment, User user);
}
