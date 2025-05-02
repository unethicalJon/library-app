package com.example.library.dto.auth;

import lombok.Data;

@Data
public class OtpRequest {
    private String username;
    private String otp;
}
