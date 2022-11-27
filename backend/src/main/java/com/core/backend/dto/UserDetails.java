package com.core.backend.dto;

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
    private boolean studentStatusConfirmed;
}