package com.core.backend.service;

import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoRoleException;
import com.core.backend.exception.NoUserException;
import com.core.backend.exception.WrongIdException;
import com.core.backend.model.Role;
import com.core.backend.model.VerificationToken;
import com.core.backend.model.User;
import com.core.backend.dto.RegisterUser;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User registerNewUserAccount(RegisterUser userDto) throws Exception;
    void createVerificationToken(User user, String token);

    void deleteVerificationToken(VerificationToken token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken getVerificationToken(User user);

    Role getRole(String id) throws NoRoleException;

    User getUserByToken(String verificationToken);

    User getUserById(String id) throws WrongIdException, NoIdException, NoUserException;

    User getUserByEmail(String email);

    User getUserByName(String name);

    User saveUser(User user);

    void deleteUser(User user);

    void deleteUnconfirmedUser(User user);

    void changeAvatar(String email, String fileExtension);

    void deleteAvatar(String email);
}
