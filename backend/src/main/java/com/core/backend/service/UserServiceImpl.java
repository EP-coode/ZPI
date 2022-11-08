package com.core.backend.service;

import com.core.backend.registration.verificationToken.VerificationToken;
import com.core.backend.registration.verificationToken.VerificationTokenRepository;
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
            throw new Exception("User: " + userDto.getEmail() + " already exists");
        }
        Optional<Role> role = roleRepository.findById("user");
        if(role.isEmpty()){
            throw new Exception("User role not exists");
        }
        User user = new User();
        user.setRole(role.get());
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
    public User saveRegisteredUser(User user) {
        return userRepository.save(user);
    }
}
