package com.core.backend.repository;

import com.core.backend.model.Post;
import com.core.backend.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
    User findByName(String name);
    List<User> findAll();
    List<User> findAll(Pageable page);
    List<User> findAllByNameContaining(String name, Pageable page);
}
