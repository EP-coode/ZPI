package com.core.backend.dto.mapper;

import com.core.backend.dto.UserDto;
import com.core.backend.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setAvatarUrl(user.getAvatarUrl());
        userDto.setStudentStatusConfirmed(user.isStudentStatusConfirmed());
        userDto.setRole(user.getRole().getRoleName().split("_")[1]);
        userDto.setEmailConfirmed(user.isEmailConfirmed());
        userDto.setId(user.getUserId());
        return userDto;
    }
}
