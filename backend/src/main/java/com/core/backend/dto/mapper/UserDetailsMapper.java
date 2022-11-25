package com.core.backend.dto.mapper;

import com.core.backend.dto.user.UserDetails;
import com.core.backend.model.User;

public class UserDetailsMapper {

    public static UserDetails toUserDetails(User user) {
        UserDetails userDetails = new UserDetails();
        userDetails.setName(user.getName());
        userDetails.setEmail(user.getEmail());
        userDetails.setAvatarUrl(user.getAvatarUrl());
        userDetails.setStudentStatus(user.isStudentStatusConfirmed() ? "TAK" : "NIE");
        userDetails.setRoleName(user.getRole().getRoleName().split("_")[1]);
        return userDetails;
    }
}
