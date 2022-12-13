package com.core.backend.dto.mapper;

import com.core.backend.dto.user.UserDto;
import com.core.backend.model.User;

public class UserMapper {

    private static final String imageUriPrefix = "https://studentcommunityimages.blob.core.windows.net/images/";

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setAvatarUrl(user.getAvatarUrl() != null ? imageUriPrefix + user.getAvatarUrl() : null);
        userDto.setPwrStatusConfirmed(user.isPwrStatusConfirmed());
        userDto.setRole(user.getRole().getRoleName().split("_")[1]);
        userDto.setEmailConfirmed(user.isEmailConfirmed());
        userDto.setId(user.getUserId());
        return userDto;
    }
}
