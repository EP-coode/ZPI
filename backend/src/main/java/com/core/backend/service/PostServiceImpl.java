package com.core.backend.service;

import com.core.backend.dto.comment.CommentCreateUpdateDto;
import com.core.backend.dto.comment.CommentDto;
import com.core.backend.dto.filter.PostFilters;
import com.core.backend.dto.post.PostCreateUpdateDto;
import com.core.backend.dto.post.PostDto;
import com.core.backend.dto.post.PostWithPaginationDto;
import com.core.backend.dto.mapper.CommentMapper;
import com.core.backend.dto.mapper.PostMapper;
import com.core.backend.exception.*;
import com.core.backend.id.PostLikeOrDislikeId;
import com.core.backend.model.*;
import com.core.backend.repository.*;
import com.core.backend.sorting.PostSorting;
import com.core.backend.utilis.Utilis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private FileService fileService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private PostCategoryRepository postCategoryRepository;

    @Autowired
    private PostLikeOrDislikeRepository postLikeOrDislikeRepository;

    @Autowired
    private Utilis utilis;

    public static final int PAGE_SIZE = 5;

    @Override
    public List<PostDto> getAllPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return postRepository.findAllPosts().stream()
                .map(p -> {
                    PostLikeOrDislike postLikeOrDislike = getPostLikeOrDislike(p, user);
                    Boolean isLiked = postLikeOrDislike == null ? null : postLikeOrDislike.isLikes();
                    return PostMapper.toPostDto(p, isLiked);
                }).collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAllPostsPagination(Integer page, Sort.Direction sort) {
        page = page == null ? 0 : page;
        Pageable pageableRequest = PageRequest.of(page, PAGE_SIZE, sort, "postId");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return postRepository.findAllPosts(pageableRequest).stream()
                .map(p -> {
                    PostLikeOrDislike postLikeOrDislike = getPostLikeOrDislike(p, user);
                    Boolean isLiked = postLikeOrDislike == null ? null : postLikeOrDislike.isLikes();
                    return PostMapper.toPostDto(p, isLiked);
                }).collect(Collectors.toList());
    }

    @Override
    public PostWithPaginationDto getPostsFiltered(PostFilters postFilters, int page, int postPerPage) {
        List<Post> posts;
        Date startDate = null, endDate = null;
        Sort sort = PostSorting.getSortingByPostOrdering(postFilters.getOrderBy());
        List<String> tagNames = List.of(postFilters.getTagNames());
        if (postFilters.getMaxPostDaysAge() != null) {
            endDate = new Date(System.currentTimeMillis());
            startDate = new Date(endDate.getTime() - postFilters.getMaxPostDaysAge() * 60000 * 60 * 24);
        }

        posts = postRepository.findPostsFiltered(postFilters.getCategoryGroupId()
                , postFilters.getCategoryId(), postFilters.getCreatorId(), startDate, endDate, sort);


        posts = posts.stream().filter(p -> {
            boolean containsAllTags = true;
            for(String tagName : tagNames) {
                if (!p.getPostTags().contains(new PostTag(tagName))) {
                    containsAllTags = false;
                    break;
                }
            }
            return containsAllTags;
        }).collect(Collectors.toList());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        int postsToSkip = page * postPerPage;

        Stream<PostDto> filteredPostsStream = posts.stream().map(p -> {
            PostLikeOrDislike postLikeOrDislike = getPostLikeOrDislike(p, user);
            Boolean isLiked = postLikeOrDislike == null ? null : postLikeOrDislike.isLikes();
            return PostMapper.toPostDto(p, isLiked);
        });

        List<PostDto> collectedPosts = filteredPostsStream
                .collect(Collectors.toList());

        long totalPostsCount = collectedPosts.size();

        collectedPosts = collectedPosts.stream()
                .skip(postsToSkip)
                .limit(postPerPage)
                .collect(Collectors.toList());

        return new PostWithPaginationDto(collectedPosts, totalPostsCount);
    }

    @Override
    public PostDto getPostByPostId(String postId) throws WrongIdException, NoIdException, NoPostException {
        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> post = postRepository.findById(longId);
        if (post.isEmpty())
            throw new NoPostException("Post nie istnieje");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        PostLikeOrDislike postLikeOrDislike = getPostLikeOrDislike(post.get(), user);
        Boolean isLiked = postLikeOrDislike == null ? null : postLikeOrDislike.isLikes();
        return PostMapper.toPostDto(post.get(), isLiked);
    }

    @Override
    public List<CommentDto> getComments(String postId) throws WrongIdException, NoIdException, NoPostException {
        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> postOptional = postRepository.findById(longId);
        if (postOptional.isEmpty())
            throw new NoPostException();
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
        if (postOptional.isEmpty())
            throw new NoPostException();
        Post post = postOptional.get();
        page = page == null ? 0 : page;
        Pageable pageableRequest = PageRequest.of(page, PAGE_SIZE, sort, "commentId");
        List<Comment> comments = commentRepository.findAllCommentsByPostId(post.getPostId(), pageableRequest);

        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostCreateUpdateDto addPost(PostCreateUpdateDto postDto, MultipartFile photo)
            throws NoPostCategoryException {
        Post post;
        boolean isPhotoEmpty = photo == null || photo.isEmpty();
        String photoPath = "";

        postDto.setTagNames(postDto.getTagNames().stream()
                .map(p -> p.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet()));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<PostTag> postTagList = StreamSupport
                .stream(postTagRepository.findAllById(postDto.getTagNames()).spliterator(), false)
                .toList();
        Set<PostTag> postTags = new HashSet<>(postTagList);

        for (String tag : postDto.getTagNames()) {
            if (!postTags.contains(new PostTag(tag))) {
                PostTag p = new PostTag(tag);
                postTagRepository.save(p);
                postTags.add(p);
            }
        }
        PostCategory postCategory = postCategoryRepository.findByDisplayName(postDto.getCategoryName());
        if (postCategory == null)
            throw new NoPostCategoryException("Podana kategoria postu nie istnieje");
        postCategory.setTotalPosts(postCategory.getTotalPosts() + 1);
        postCategory.getPostCategoryGroup().setTotalPosts(postCategory.getPostCategoryGroup().getTotalPosts() + 1);

        post = PostMapper.toPost(postDto, user, postCategory, postTags);
        for (PostTag p : postTags) {
            post.addPostTag(p);
        }
        post = postRepository.save(post);

        if (!isPhotoEmpty) {
            photoPath = String.format("post_%d", post.getPostId());
            post.setImageUrl(photoPath);
            postRepository.save(post);
        }

        if (isPhotoEmpty)
            return postDto;

        fileService.uploadFile(photo, photoPath);
        return postDto;
    }

    @Override
    public void updatePost(String postId, PostCreateUpdateDto postDto, MultipartFile photo)
            throws NoAccessException, NoPostException, WrongIdException, NoIdException, NoPostCategoryException {
        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> postOpt = postRepository.findById(longId);
        if (postOpt.isEmpty())
            throw new NoPostException("Post nie istnieje");
        Post post = postOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.equals(authentication.getName(),
                post.getCreator().getEmail()) &&
                authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
            throw new NoAccessException("Próba edycji nie swojego postu");
        }
        postDto.setTagNames(postDto.getTagNames().stream()
                .map(p -> p.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet()));
        PostCategory newPostCategory = postCategoryRepository.findByDisplayName(postDto.getCategoryName());
        if (newPostCategory == null)
            throw new NoPostCategoryException("Podana kategoria postu nie istnieje");
        if (!Objects.equals(post.getCategory().getDisplayName(), newPostCategory.getDisplayName())) {
            PostCategory oldPostCategory = post.getCategory();
            oldPostCategory.setTotalPosts(oldPostCategory.getTotalPosts() - 1);
            newPostCategory.setTotalPosts(newPostCategory.getTotalPosts() + 1);
            if (!Objects.equals(oldPostCategory.getPostCategoryGroup().getDisplayName(), newPostCategory
                    .getPostCategoryGroup().getDisplayName())) {
                oldPostCategory.getPostCategoryGroup()
                        .setTotalPosts(oldPostCategory.getPostCategoryGroup().getTotalPosts() - 1);
                newPostCategory.getPostCategoryGroup()
                        .setTotalPosts(newPostCategory.getPostCategoryGroup().getTotalPosts() + 1);
            }
        }
        boolean isPhotoEmpty = photo == null || photo.isEmpty();
        String path = isPhotoEmpty ? "" : String.format("post_%d", longId);
        post.setCategory(newPostCategory);
        post.setImageUrl(path);
        post.setMarkdownContent(postDto.getMarkdownContent());
        post.setTitle(postDto.getTitle());

        List<PostTag> newPostTags = StreamSupport
                .stream(postTagRepository.findAllById(postDto.getTagNames()).spliterator(), false).toList();

        for (String tag : postDto.getTagNames()) {
            int id = newPostTags.indexOf(new PostTag(tag));
            PostTag p = new PostTag(tag);
            if (id != -1) {
                p = newPostTags.get(id);
                postTagRepository.save(p);
            }
            if (!post.getPostTags().contains(p)) {
                postTagRepository.save(p);
                post.addPostTag(p);
            }
        }
        Set<PostTag> oldPostTags = new HashSet<>(post.getPostTags());
        for (PostTag postTag : oldPostTags) {
            if (!newPostTags.contains(postTag)) {
                post.deletePostTag(postTag);
            }
        }
        postRepository.save(post);

        if (isPhotoEmpty)
            return;
        fileService.uploadFile(photo, path);
    }

    @Override
    public void deletePost(String postId) throws NoAccessException, WrongIdException, NoIdException {
        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> post = postRepository.findById(longId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (post.isEmpty()) {
        } else if ((!Objects.equals(authentication.getName(),
                post.get().getCreator().getEmail())) &&
                authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER")))
            throw new NoAccessException("Możesz usunąć tylko swój post");
        else {
            Post postToDelete = post.get();
            String photoToDelete = String.format("post_%s", postId);
            List<PostTag> postTagList = postTagRepository.findPostTagsByPostsPostId(postToDelete.getPostId()).stream()
                    .toList();
            Set<PostTag> postTags = new HashSet<>(postTagList);
            for (PostTag p : postTags)
                postToDelete.deletePostTag(p);
            List<PostLikeOrDislikeId> postLikeOrDislikes = postLikeOrDislikeRepository
                    .findAllByPostId(postToDelete.getPostId())
                    .stream()
                    .map(PostLikeOrDislike::getPostLikeOrDislikeId)
                    .collect(Collectors.toList());
            postToDelete.getCategory().getPostCategoryGroup()
                    .setTotalPosts(postToDelete.getCategory().getPostCategoryGroup().getTotalPosts() - 1);
            postToDelete.getCategory().setTotalPosts(postToDelete.getCategory().getTotalPosts() - 1);
            postLikeOrDislikeRepository.deleteAllById(postLikeOrDislikes);
            postRepository.deleteById(longId);
            fileService.deleteFile(photoToDelete);
        }
    }

    @Override
    public CommentCreateUpdateDto addComment(String postId, CommentCreateUpdateDto commentCreateUpdateDto)
            throws NoPostException, WrongIdException, NoIdException {
        long longId;
        longId = utilis.convertId(postId);
        User creator = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Post> post = postRepository.findById(longId);
        if (post.isEmpty())
            throw new NoPostException("Post nie istnieje");
        commentRepository.save(CommentMapper.toComment(commentCreateUpdateDto, creator, post.get()));
        return commentCreateUpdateDto;
    }

    @Override
    public void updateComment(String postId, String commentId, CommentCreateUpdateDto commentUpdateDto)
            throws NoPostException, NoAccessException, WrongIdException, NoIdException, NoCommentException {
        long longPostId, longCommentId;
        longPostId = utilis.convertId(postId);
        longCommentId = utilis.convertId(commentId);
        Optional<Post> post = postRepository.findById(longPostId);
        if (post.isEmpty())
            throw new NoPostException("Post nie istnieje");
        Optional<Comment> comment = commentRepository.findById(longCommentId);
        if (comment.isEmpty())
            throw new NoCommentException("Komentarz nie istnieje");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!comment.get().getCreator().getEmail().equals(authentication.getName()) &&
                authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER")))
            throw new NoAccessException("Możesz zaktualizować tylko swój komentarz");
        comment.get().setContent(commentUpdateDto.getContent());
        commentRepository.save(comment.get());
    }

    @Override
    public void deleteComment(String commentId) throws NoAccessException, WrongIdException, NoIdException {
        long longId;
        longId = utilis.convertId(commentId);
        Optional<Comment> comment = commentRepository.findById(longId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (comment.isEmpty()) {
        } else if ((!Objects.equals(authentication.getName(),
                comment.get().getCreator().getEmail()))
                && authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER")))
            throw new NoAccessException("Możesz usunąć tylko swój komentarz");
        else
            commentRepository.deleteById(longId);
    }

    @Override
    public byte[] getPhotoByPostId(String postId) throws NoIdException, NoPostException, WrongIdException {
        long longId;
        longId = utilis.convertId(postId);
        Optional<Post> post = postRepository.findById(longId);
        if (post.isEmpty())
            throw new NoPostException("Post nie istnieje");
        String fileName = String.format("post_%d", longId);
        return fileService.downloadFile(fileName);
    }

    @Override
    public PostLikeOrDislike getPostLikeOrDislike(Post post, User user) {
        if (user == null)
            return null;
        PostLikeOrDislikeId postLikeOrDislikeId = new PostLikeOrDislikeId(user, post);
        Optional<PostLikeOrDislike> postLikeOrDislike = postLikeOrDislikeRepository.findById(postLikeOrDislikeId);
        return postLikeOrDislike.isEmpty() ? null : postLikeOrDislike.get();
    }

}
