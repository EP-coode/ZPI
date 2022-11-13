package com.core.backend.service;

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

    User getUserByToken(String verificationToken);

    User getUserByEmail(String email);

    User saveUser(User user);

    void changeAvatar(String email, String fileExtension);

    void deleteAvatar(String email);
}
