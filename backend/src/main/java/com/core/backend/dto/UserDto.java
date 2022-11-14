package com.core.backend.dto;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;
    private String avatarUrl;
    private String roleName;
    private String studentStatus;
}
