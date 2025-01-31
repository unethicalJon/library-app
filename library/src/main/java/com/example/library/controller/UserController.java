package com.example.library.controller;

import com.example.library.dto.auth.AuthResponseDto;
import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.user.UserDto;
import com.example.library.entity.User;
import com.example.library.security.JwtGenerator;
import com.example.library.service.UserService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.UserController.BASE)
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtGenerator jwtGenerator;

    @PostMapping(RestConstants.UserController.ADD_USER)
    public ResponseEntity<EntityIdDto> postUser(@RequestBody UserDto addUserRequest) {
        User user = userService.postUser(addUserRequest);
        return new ResponseEntity<>(EntityIdDto.of(user.getId()), HttpStatus.CREATED);
    }

    @PostMapping(RestConstants.UserController.AUTHENTICATE_USER)
    public ResponseEntity<AuthResponseDto> logUser(@RequestBody UserDto logUserRequest) {
            // Try to authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logUserRequest.getUsername(),
                            logUserRequest.getPassword()));

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate the JWT token
            String token = jwtGenerator.generateToken(authentication);
            return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);


        }
}
