package com.core.backend.repository;

import com.core.backend.model.Post;
import com.core.backend.model.PostTag;
import com.core.backend.model.PostTags;
import com.core.backend.id.PostTagsId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostTagsRepository extends CrudRepository<PostTags, PostTagsId> {
    List<PostTags> findByPostTagsIdPostId(Post post);
    List<PostTags> findAllByPostTagsIdPostIdIn(List<Post> postList);
}
