package com.example.library.controller;

import com.example.library.dto.auth.AuthenticationRequestDto;
import com.example.library.dto.auth.AuthenticationResponseDto;
import com.example.library.service.UserService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.AuthController.BASE)
public class AuthController {

    private final UserService userService;

    @PostMapping(RestConstants.AuthController.LOG_IN)
    public ResponseEntity<AuthenticationResponseDto> logUser(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        String jwt = userService.createJwtToken(authenticationRequestDto);
        return ResponseEntity.ok(new AuthenticationResponseDto(jwt));
    }

}
