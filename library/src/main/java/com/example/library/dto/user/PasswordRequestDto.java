package com.example.library.dto.user;

import lombok.Data;

@Data
public class PasswordRequestDto {

    private String newPassword;

    private String confirmationPassword;
}
