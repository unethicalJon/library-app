package com.example.library.controller;

import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.user.UserDto;
import com.example.library.dto.user.SimpleUserDto;
import com.example.library.entity.User;
import com.example.library.service.UserService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.UserController.BASE)
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(RestConstants.UserController.SIGN_UP)
    public ResponseEntity<EntityIdDto> postUser(@RequestBody UserDto addUserRequest) {
        User user = userService.postUser(addUserRequest);
        return new ResponseEntity<>(EntityIdDto.of(user.getId()), HttpStatus.CREATED);
    }

    @GetMapping(RestConstants.ID_PATH)
    public ResponseEntity<SimpleUserDto> getUserProfile(@PathVariable(value = RestConstants.ID) Long id) {
        SimpleUserDto userDto = userService.getUserProfile(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping(RestConstants.UserController.UPDATE + RestConstants.ID_PATH)
    public ResponseEntity<User> updateUserProfile(@PathVariable(value = RestConstants.ID) Long id, @RequestBody SimpleUserDto updatedUser) {
        User user = userService.updateUserProfile(id, updatedUser);
        return new ResponseEntity(EntityIdDto.of(user.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(RestConstants.UserController.PASSWORD_CHANGE + RestConstants.ID_PATH)
    public ResponseEntity<User> adminUpdateUserPassword(@PathVariable(value = RestConstants.ID) Long id,
                                                        @RequestBody User adminUpdatedUserPassword) {
        try {
            User updatedUser = userService.adminUpdateUserPassword(id, adminUpdatedUserPassword);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(RestConstants.UserController.ACTIVATE_USER + RestConstants.ID_PATH)
    public ResponseEntity<String> adminActivateUser(@PathVariable(value = RestConstants.ID) Long id) {
        userService.adminActivateUser(id);
        return ResponseEntity.ok("User activated successfully.");
    }
}





