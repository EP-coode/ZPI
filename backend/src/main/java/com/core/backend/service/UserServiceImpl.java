package com.core.backend.service;

import com.core.backend.exception.*;
import com.core.backend.id.FollowedUserId;
import com.core.backend.model.FollowedUser;
import com.core.backend.model.Role;
import com.core.backend.repository.FollowedUserRepository;
import com.core.backend.repository.RoleRepository;
import com.core.backend.model.User;
import com.core.backend.repository.UserRepository;
import com.core.backend.utilis.Utilis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FollowedUserRepository followedUserRepository;
    public static final int PAGE_SIZE = 5;

    @Autowired
    private Utilis utilis;

    @Override
    public Role getRole(String id) throws NoRoleException {
        Optional<Role> role = roleRepository.findById(id);
        if(role.isEmpty()) throw new NoRoleException();
        return role.get();
    }

    @Override
    public List<User> getAllUsers(String name, Integer page, Sort.Direction sort) {
        page = page == null ? 0 : page;
        Pageable pageableRequest = PageRequest.of(page, PAGE_SIZE, sort, "name");
        return name == null ? userRepository.findAll(pageableRequest) : userRepository.findAllByNameContaining(name, pageableRequest);
    }

    @Override
    public User getUserById(String id) throws WrongIdException, NoIdException, NoUserException {
        long longId = utilis.convertId(id);
        Optional<User> user = userRepository.findById(longId);
        if (user.isEmpty()) throw new NoUserException();
        return user.get();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user){
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

    @Override
    public void followUnfollowUser(String email, String id) throws WrongIdException, NoUserException, NoIdException {
        User user = userRepository.findByEmail(email);
        User userToFollow = getUserById(id);
        FollowedUserId followedUserId = new FollowedUserId();
        followedUserId.setUserId(user);
        followedUserId.setFollowedUserId(userToFollow);
        Optional<FollowedUser> followedUser = followedUserRepository.findById(followedUserId);
        if(followedUser.isPresent()){
            followedUserRepository.delete(followedUser.get());
        }else{
            FollowedUser newFollow = new FollowedUser();
            newFollow.setPrimaryKey(followedUserId);
            followedUserRepository.save(newFollow);
        }
    }

    @Override
    public List<User> getFollowers(String id) throws WrongIdException, NoUserException, NoIdException {
        User user = getUserById(id);
        return followedUserRepository.findAllByPrimaryKey_FollowedUserId(user)
                .stream()
                .map(FollowedUser::getPrimaryKey)
                .map(FollowedUserId::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFollowings(String id) throws WrongIdException, NoUserException, NoIdException {
        User user = getUserById(id);
        return followedUserRepository.findAllByPrimaryKey_UserId(user)
                .stream()
                .map(FollowedUser::getPrimaryKey)
                .map(FollowedUserId::getFollowedUserId)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        else{
            log.info("User found in the database: {}", username);
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
}
