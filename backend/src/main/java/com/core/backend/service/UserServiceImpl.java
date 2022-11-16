package com.core.backend.service;

import com.core.backend.model.VerificationToken;
import com.core.backend.repository.VerificationTokenRepository;
import com.core.backend.model.Role;
import com.core.backend.repository.RoleRepository;
import com.core.backend.model.User;
import com.core.backend.repository.UserRepository;
import com.core.backend.dto.RegisterUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service @RequiredArgsConstructor @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        else{
            log.info("User found in the database: {}", email);
        }
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                user.isEmailConfirmed(),
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);
    }

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
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public void deleteVerificationToken(VerificationToken token) {
        tokenRepository.deleteById(token.getId());
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(User user) {
        return tokenRepository.findByUser(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }


    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUnconfirmedUser(User user) {
        VerificationToken token = getVerificationToken(user);
        if (token != null)
            deleteVerificationToken(token);
        userRepository.deleteById(user.getUserId());
    }

    @Override
    public void changeAvatar(String email, String fileExtension) {
        String newFileName = "avatar_" + email + "." + fileExtension;
        User user = getUserByEmail(email);
        user.setAvatarUrl(newFileName);
        saveUser(user);
    }

    @Override
    public void deleteAvatar(String email) {
        User user = getUserByEmail(email);
        user.setAvatarUrl(null);
        saveUser(user);
    }
}