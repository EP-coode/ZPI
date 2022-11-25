package com.core.backend.dto.mapper;

import com.core.backend.dto.user.UserDto;
import com.core.backend.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setAvatarUrl(user.getAvatarUrl());
        userDto.setStudentStatus(user.isStudentStatusConfirmed() ? "TAK" : "NIE");
        userDto.setRoleName(user.getRole().getRoleName().split("_")[1]);
        return userDto;
    }
}
