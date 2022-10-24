package com.core.backend.Service;

import com.core.backend.Registration.VerificationToken.VerificationToken;
import com.core.backend.User.User;
import com.core.backend.dto.RegisterUser;

public interface UserService {

    public User registerNewUserAccount(RegisterUser userDto) throws Exception;
    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    User getUser(String verificationToken);

    User saveRegisteredUser(User user);
}
