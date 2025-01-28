package com.example.library.controller;

import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.user.UserDto;
import com.example.library.entity.User;
import com.example.library.service.UserService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.UserController.BASE)
public class UserController {

    private final UserService userService;

    @PostMapping(RestConstants.UserController.ADD_USER)
    public ResponseEntity<EntityIdDto> postUser(@RequestBody UserDto addUserRequest) {
        User user = userService.postUser(addUserRequest);
        return new ResponseEntity<>(EntityIdDto.of(user.getId()), HttpStatus.CREATED);
    }
}
