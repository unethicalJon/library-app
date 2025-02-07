package com.example.library.security;

public class UserUtil {

    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

    public static final String PASSWORD_MESSAGE = "Password must contain at least 8 characters, including at least a " +
            "lowercase letter, an uppercase letter and a digit!";

}
