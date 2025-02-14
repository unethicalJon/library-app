package com.example.library.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

    public static final String PASSWORD_MESSAGE = "Password must contain at least 8 characters, including at least a " +
            "lowercase letter, an uppercase letter and a digit!";

    public static CustomUserDetails getLoggedInUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

}
