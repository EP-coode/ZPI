package com.core.backend.dto.user;

import com.core.backend.model.User;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String name;
    private Boolean emailConfirmed;
    private String avatarUrl;
    private String role;
    private Boolean studentStatusConfirmed;
}
