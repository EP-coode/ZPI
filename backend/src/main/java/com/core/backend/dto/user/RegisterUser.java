package com.core.backend.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegisterUser {

    @NotEmpty
    @Size(min = 2, message = "name should have at least 2 characters")
    private String name;
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6, message = "password should have at least 6 characters")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
