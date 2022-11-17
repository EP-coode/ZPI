package com.core.backend.service;


import com.core.backend.dto.comment.CommentCreateUpdateDto;
import com.core.backend.dto.comment.CommentDto;
import com.core.backend.dto.filter.PostFilters;
import com.core.backend.dto.post.PostCreateUpdateDto;
import com.core.backend.dto.post.PostDto;
import com.core.backend.exception.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostDto> getAllPosts();

    List<PostDto> getAllPostsPagination(Integer page, Sort.Direction sort);

    List<PostDto> getPostsFiltered(PostFilters postFilters);

    PostDto getPostByPostId(String postId) throws WrongIdException, NoIdException, NoPostException;

    List<CommentDto> getComments(String postId) throws WrongIdException, NoIdException, NoPostException;

    List<CommentDto> getCommentsPagination(String postId, Integer page, Sort.Direction sort) throws WrongIdException, NoIdException, NoPostException;

    PostCreateUpdateDto addPost(PostCreateUpdateDto postDto, MultipartFile photo) throws NoAccessException;

    void updatePost(String postId, PostCreateUpdateDto postDto, MultipartFile photo) throws NoAccessException, NoPostException, WrongIdException, NoIdException;

    void deletePost(String postId) throws NoAccessException, WrongIdException, NoIdException;

    CommentCreateUpdateDto addComment(String postId, CommentCreateUpdateDto commentCreateUpdateDto) throws NoPostException, WrongIdException, NoIdException;

    void updateComment(String postId, String commentId, CommentCreateUpdateDto commentCreateUpdateDto) throws NoAccessException, WrongIdException, NoIdException, NoPostException, NoCommentException;

    void deleteComment(String commentId) throws NoAccessException, WrongIdException, NoIdException;

    ByteArrayResource getPhotoByPostId(String postId) throws NoIdException, NoPostException, WrongIdException;
}
