package com.example.library.controller;

import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.user.PasswordRequestDto;
import com.example.library.dto.user.UserDetailsDto;
import com.example.library.dto.user.UserDto;
import com.example.library.entity.User;
import com.example.library.service.UserService;
import com.example.library.util.apiDocs.UserControllerDoc;
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

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PostMapping(RestConstants.UserController.SIGN_UP)
    @UserControllerDoc.RegisterUserDoc
    public ResponseEntity<EntityIdDto> postUser(@RequestBody UserDto addUserRequest) {
        User user = userService.postUser(addUserRequest);
        return new ResponseEntity<>(EntityIdDto.of(user.getId()), HttpStatus.CREATED);
    }

    @GetMapping(RestConstants.ID_PATH)
    @UserControllerDoc.GetUserProfileDoc
    public ResponseEntity<UserDetailsDto> getUserProfile(@PathVariable(value = RestConstants.ID) Long id) {
        UserDetailsDto userDto = userService.getUserProfile(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping(RestConstants.UserController.UPDATE)
    @UserControllerDoc.UpdateUserProfileDoc
    public ResponseEntity<EntityIdDto> updateUserProfile(@PathVariable(value = RestConstants.ID) Long id, @RequestBody UserDetailsDto updatedUser) {
        User user = userService.updateUserProfile(id, updatedUser);
        return new ResponseEntity<>(EntityIdDto.of(user.getId()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PutMapping(RestConstants.UserController.PASSWORD_CHANGE)
    @UserControllerDoc.UpdateUserPasswordDoc
    public ResponseEntity<EntityIdDto> updateUserPassword(@PathVariable(value = RestConstants.ID) Long id,
                                                          @RequestBody PasswordRequestDto passwordRequestDto) {
        User user = userService.updateUserPassword(id, passwordRequestDto);
        return new ResponseEntity<>(EntityIdDto.of(user.getId()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PutMapping(RestConstants.UserController.ACTIVATE_USER)
    @UserControllerDoc.ActivateUserDoc
    public ResponseEntity<String> adminActivateUser(@PathVariable(value = RestConstants.ID) Long id) {
        userService.adminActivateUser(id);
        return ResponseEntity.ok("User activated successfully.");
    }

    @PreAuthorize("hasAnyAuthority(@Role.USER_NAME)")
    @PutMapping(RestConstants.UserController.UPDATE_LIBRARY)
    public ResponseEntity<EntityIdDto> updateLibrary(@RequestBody UserDto userDto) {
        User user = userService.changeLibrary(userDto);
        return new ResponseEntity<>(EntityIdDto.of(user.getId()), HttpStatus.CREATED);
    }
}






