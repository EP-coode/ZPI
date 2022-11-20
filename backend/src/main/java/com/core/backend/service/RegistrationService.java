package com.core.backend.service;

import com.core.backend.dto.RegisterUser;
import com.core.backend.exception.NoUserException;
import com.core.backend.exception.NoVerificationTokenException;
import com.core.backend.exception.TokenExpiredException;
import com.core.backend.model.User;
import com.core.backend.model.VerificationToken;

public interface RegistrationService {

    User registerNewUserAccount(RegisterUser userDto) throws Exception;

    void confirmUser(String token) throws NoVerificationTokenException, TokenExpiredException, NoUserException;

    void deleteUnconfirmedUser(User user);

    void createVerificationToken(User user);

    VerificationToken getVerificationToken(User user);

    User resetVerificationToken(String email) throws NoUserException;

}
