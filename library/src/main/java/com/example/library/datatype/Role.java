package com.example.library.datatype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.library.security.RoleName.ADMIN_NAME;
import static com.example.library.security.RoleName.USER_NAME;

@Getter
@AllArgsConstructor
public enum Role {

    USER(USER_NAME), ADMIN(ADMIN_NAME);
    public String name;
}
