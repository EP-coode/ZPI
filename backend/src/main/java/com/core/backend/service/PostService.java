package com.core.backend.service;


import com.core.backend.dto.CommentDto;
import com.core.backend.dto.PostDto;
import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.WrongIdException;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PostService {
    List<PostDto> getAllPosts();

    List<PostDto> getAllPostsPagination(Integer page, Sort.Direction sort);

//    List<PostDto> getAllPostsFiltered();

    PostDto getPostByPostId(String postId) throws WrongIdException, NoIdException, NoPostException;

    List<CommentDto> getComments(String postId) throws WrongIdException, NoIdException, NoPostException;

    List<CommentDto> getCommentsPagination(String postId, Integer page, Sort.Direction sort) throws WrongIdException, NoIdException, NoPostException;

}
