package com.core.backend.service;

import com.core.backend.dto.RegisterUser;
import com.core.backend.exception.NoUserException;
import com.core.backend.exception.NoVerificationTokenException;
import com.core.backend.exception.TokenExpiredException;
import com.core.backend.model.Role;
import com.core.backend.model.User;
import com.core.backend.model.VerificationToken;
import com.core.backend.repository.RoleRepository;
import com.core.backend.repository.UserRepository;
import com.core.backend.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User registerNewUserAccount(RegisterUser userDto) throws Exception{
        if(userRepository.findByEmail(userDto.getEmail()) != null){
            throw new IllegalArgumentException("Użytkownik o emailu: " + userDto.getEmail() + " już istnieje");
        }
        if (userRepository.findByName(userDto.getName()) != null) {
            throw new IllegalArgumentException("Użytkownik o nazwie: " + userDto.getName() + " już istnieje");
        }
        Optional<Role> role = roleRepository.findById("ROLE_USER");
        if(role.isEmpty()){
            throw new Exception("User role not exists");
        }
        User user = new User();
        user.setRole(role.get());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void confirmUser(String token) throws NoVerificationTokenException, TokenExpiredException {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new NoVerificationTokenException();
        }
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new TokenExpiredException();
        }
        User user = verificationToken.getUser();
        user.setEmailConfirmed(true);
        userRepository.save(user);
        tokenRepository.deleteById(verificationToken.getId());
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public User resetVerificationToken(String email) throws NoUserException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new NoUserException();
        }
        if(user.isEmailConfirmed()){
            throw new IllegalArgumentException();
        }
        VerificationToken verificationToken = tokenRepository.findByUser(user);
        if(verificationToken != null){
            tokenRepository.deleteById(verificationToken.getId());
        }
        return user;
    }

    @Override
    public void deleteUnconfirmedUser(User user) {
        VerificationToken token = tokenRepository.findByUser(user);
        if (token != null)
            tokenRepository.deleteById(token.getId());
        userRepository.delete(user);
    }
}
