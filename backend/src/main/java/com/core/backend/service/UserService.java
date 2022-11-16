package com.core.backend.service;

import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoRoleException;
import com.core.backend.exception.NoUserException;
import com.core.backend.exception.WrongIdException;
import com.core.backend.model.Role;
import com.core.backend.model.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {

    Role getRole(String id) throws NoRoleException;

    List<User> getAllUsers(String name, Integer page, Sort.Direction sort);

    User getUserById(String id) throws WrongIdException, NoIdException, NoUserException;

    User getUserByEmail(String email);

    User getUserByName(String name);

    User saveUser(User user);

    void deleteUser(User user);

    void changeAvatar(String email, String fileExtension);

    void deleteAvatar(String email);
}
