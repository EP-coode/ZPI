package com.core.backend.dto.user;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
    private String name;
    private String email;
    private String avatarUrl;
    private String roleName;
    private String studentStatus;
}