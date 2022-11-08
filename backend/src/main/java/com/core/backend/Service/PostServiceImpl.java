package com.core.backend.service;

import com.core.backend.dto.CommentDto;
import com.core.backend.dto.PostDto;
import com.core.backend.dto.mapper.CommentMapper;
import com.core.backend.dto.mapper.PostMapper;
import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.WrongIdException;
import com.core.backend.model.Comment;
import com.core.backend.model.Post;
import com.core.backend.repository.CommentRepository;
import com.core.backend.repository.PostRepository;
import com.core.backend.utilis.Utilis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private Utilis utilis;

    public static final int PAGE_SIZE = 5;


    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAllPosts().stream()
                .map(PostMapper::toPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAllPostsPagination(Integer page, Sort.Direction sort) {
        page = page == null ? 0 : page;
        Pageable pageableRequest = PageRequest.of(page, PAGE_SIZE, sort, "postId");
        return postRepository.findAllPosts(pageableRequest).stream()
                .map(PostMapper::toPostDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getPostByPostId(String postId) throws WrongIdException, NoIdException, NoPostException {
        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> post = postRepository.findById(longId);
        if (post.isEmpty()) throw new NoPostException();
        return PostMapper.toPostDto(postRepository.findById(longId).get());
    }

    @Override
    public List<CommentDto> getComments(String postId) throws WrongIdException, NoIdException, NoPostException {
        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> postOptional = postRepository.findById(longId);
        if (postOptional.isEmpty()) throw new NoPostException();
        Post post = postOptional.get();
        List<Comment> comments = commentRepository.findAllCommentsByPostId(post.getPostId());
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsPagination(String postId, Integer page, Sort.Direction sort)
            throws WrongIdException, NoIdException, NoPostException {

        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> postOptional = postRepository.findById(longId);
        if (postOptional.isEmpty()) throw new NoPostException();
        Post post = postOptional.get();
        page = page == null ? 0 : page;
        Pageable pageableRequest = PageRequest.of(page, PAGE_SIZE, sort, "commentId");
        List<Comment> comments = commentRepository.findAllCommentsByPostId(post.getPostId(), pageableRequest);

        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

}
