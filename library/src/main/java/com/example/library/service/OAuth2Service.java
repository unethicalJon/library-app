package com.example.library.service;

import com.example.library.datatype.Role;
import com.example.library.entity.User;
import com.example.library.repository.UserRepository;
import com.example.library.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public String handleOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new IllegalArgumentException("Email not found in Google account");
        }

        // Extract username from email
        String username = email.split("@")[0];

        Optional<User> existingUser = userRepository.findByUsername(username);

        User user = existingUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setName(oAuth2User.getAttribute("given_name"));
            newUser.setSurname(oAuth2User.getAttribute("family_name"));
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // dummy password
            newUser.setRole(Role.USER);
            newUser.setActive(true);

            return userRepository.save(newUser);
        });

        // Generate OTP and store it for user
        String otp = String.format("%06d", (int) (Math.random() * 1_000_000));
        otpService.storeOtp(user.getUsername(), otp);

        // Send OTP via email
        otpService.sendOtpEmail(user.getName(), user.getEmail(), otp);

        // Generate temporary token for 2FA
        return jwtUtil.generateTemporary2FAToken(user.getUsername());
    }
}

