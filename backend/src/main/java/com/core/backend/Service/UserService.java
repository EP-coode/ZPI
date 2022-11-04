package com.core.backend.Service;

import com.core.backend.Registration.VerificationToken.VerificationToken;
import com.core.backend.User.User;
import com.core.backend.dto.RegisterUser;

public interface UserService {

    public User registerNewUserAccount(RegisterUser userDto) throws Exception;
    void createVerificationToken(User user, String token);

    void deleteVerificationToken(VerificationToken token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken getVerificationToken(User user);

    User getUserByToken(String verificationToken);

    User getUserByEmail(String email);

    User saveRegisteredUser(User user);

    void deleteUnconfirmedUser(User user);
}
