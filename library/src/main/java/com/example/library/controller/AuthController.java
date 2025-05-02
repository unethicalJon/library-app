package com.example.library.controller;

import com.example.library.dto.auth.AuthenticationRequestDto;
import com.example.library.dto.auth.AuthenticationResponseDto;
import com.example.library.dto.auth.OtpRequest;
import com.example.library.security.CustomUserDetailService;
import com.example.library.security.JwtUtil;
import com.example.library.service.OtpService;
import com.example.library.service.UserService;
import com.example.library.util.apiDocs.AuthControllerDoc;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.AuthController.BASE)
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;

    @PostMapping(RestConstants.AuthController.LOG_IN)
    @AuthControllerDoc.AuthenticationDoc
    public ResponseEntity<AuthenticationResponseDto> logUser(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        String jwt = userService.createJwtToken(authenticationRequestDto);
        return ResponseEntity.ok(new AuthenticationResponseDto(jwt));
    }

    @PostMapping(RestConstants.AuthController.VERIFY_2FA)
    public ResponseEntity<?> verify2FA(@RequestBody OtpRequest request) {
        boolean isValid = otpService.verifyOtp(request.getUsername(), request.getOtp());

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
        }

        UserDetails userDetails = customUserDetailService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
