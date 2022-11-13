package com.core.backend.repository;

import com.core.backend.model.PostTags;
import com.core.backend.id.PostTagsId;
import org.springframework.data.repository.CrudRepository;

public interface PostTagsRepository extends CrudRepository<PostTags, PostTagsId> {
}
