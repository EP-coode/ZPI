package com.core.backend.service;

import com.core.backend.dto.comment.CommentCreateUpdateDto;
import com.core.backend.dto.comment.CommentDto;
import com.core.backend.dto.post.PostCreateUpdateDto;
import com.core.backend.dto.post.PostDto;
import com.core.backend.dto.mapper.CommentMapper;
import com.core.backend.dto.mapper.PostMapper;
import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.WrongIdException;
import com.core.backend.id.PostTagsId;
import com.core.backend.model.*;
import com.core.backend.repository.*;
import com.core.backend.utilis.Utilis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private PostTagsRepository postTagsRepository;

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
        return PostMapper.toPostDto(post.get());
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

    @Override
    public PostCreateUpdateDto addPost(PostCreateUpdateDto postDto) {
        Post post;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        post = PostMapper.toPost(postDto, user);
        post = postRepository.save(post);
        Iterable<PostTag> postTags = postTagRepository.findAllById(postDto.getTags().stream()
                .map(PostTag::getTagName)
                .collect(Collectors.toList()));

        List<PostTag> postTagList = StreamSupport.stream(postTags.spliterator(), false).toList();
        for (PostTag tag: postDto.getTags()) {
            if (!postTagList.contains(tag))  postTagRepository.save(tag);
            postTagsRepository.save(
                    new PostTags(new PostTagsId(post, tag)));
        }
        return postDto;
    }

    @Override
    public void updatePost(String postId, PostCreateUpdateDto postDto) throws Exception {
        //check if user is creator of the post in database
        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> postOpt = postRepository.findById(longId);
        if (postOpt.isEmpty())  throw new NoPostException("Post nie istnieje");
        Post post = postOpt.get();
        if (!Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(),
                post.getCreator().getEmail())) {
            throw new Exception("Próba edycji nie swojego postu");
        }
        post.setCategory(postDto.getCategory());
        post.setImageUrl(postDto.getImageUrl());
        post.setMarkdownContent(postDto.getMarkdownContent());
        post.setTitle(postDto.getTitle());
        postRepository.save(post);
        //fileservice delete photo
    }

    @Override
    public void deletePost(String postId) throws Exception {
        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> post = postRepository.findById(longId);
        if (post.isEmpty()) {}
        else if ((!Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(),
                post.get().getCreator().getEmail())))   throw new Exception("Możesz usunąć tylko swój post");
        else {
            List<PostTags> postTags = postTagsRepository.findByPostTagsIdPostId(post.get());
            for (PostTags p : postTags) postTagsRepository.deleteById(p.getPostTagsId());
            postRepository.deleteById(longId);
        }
        //fileservice delete photo
    }

    @Override
    public CommentCreateUpdateDto addComment(String postId, CommentCreateUpdateDto commentCreateUpdateDto) throws NoPostException, WrongIdException, NoIdException {
        long longId;
        longId = utilis.convertId(postId);
        User creator = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Post> post = postRepository.findById(longId);
        if (post.isEmpty())  throw new NoPostException("Post nie istnieje");
        commentRepository.save(CommentMapper.toComment(commentCreateUpdateDto, creator, post.get()));
        return commentCreateUpdateDto;
    }

    @Override
    public void updateComment(String postId, String commentId, CommentCreateUpdateDto commentUpdateDto) throws Exception {
        long longPostId, longCommentId;
        longPostId = utilis.convertId(postId);
        longCommentId = utilis.convertId(commentId);
        Optional<Post> post = postRepository.findById(longPostId);
        if (post.isEmpty())  throw new NoPostException("Post nie istnieje");
        Optional<Comment> comment = commentRepository.findById(longCommentId);
        if (comment.isEmpty())  throw new Exception("Komentarz nie istnieje");
        comment.get().setContent(commentUpdateDto.getContent());
        commentRepository.save(comment.get());
    }

    @Override
    public void deleteComment(String commentId) throws Exception {
        long longId;
        longId = utilis.convertId(commentId);
        Optional<Comment> comment = commentRepository.findById(longId);
        if (comment.isEmpty()) {}
        else if ((!Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(),
                comment.get().getCreator().getEmail())))   throw new Exception("Możesz usunąć tylko swój komentarz");
        else commentRepository.deleteById(longId);
    }


}
