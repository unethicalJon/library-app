package com.example.library.dto.user;

import com.example.library.dto.library.LibraryDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "name is required!")
    private String name;

    @NotBlank(message = "surname is required!")
    private String surname;

    @NotBlank(message = "email is required!")
    @Email(message = "invalid email format!")
    private String email;

    @NotBlank(message = "username is required!")
    private String username;

    @NotBlank(message = "password is required!")
    private String password;

    private String role;

    private boolean active;

    private LibraryDto library;
}
