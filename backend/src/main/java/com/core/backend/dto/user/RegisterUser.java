package com.core.backend.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegisterUser {

    @NotEmpty()
    @Size(min = 3, message = "Nazwa musi mieć przynajmniej 3 znaki")
    private String name;
    @NotEmpty()
    @Email()
    private String email;

    @NotEmpty()
    @Size(min = 7, max = 32, message = "Hasło musi zawierać od 7 do 32 znaków")
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
