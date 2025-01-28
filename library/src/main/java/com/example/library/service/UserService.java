package com.example.library.service;

import com.example.library.datatype.Role;
import com.example.library.dto.user.UserDto;
import com.example.library.entity.Library;
import com.example.library.entity.User;
import com.example.library.repository.UserRepository;
import com.example.library.security.UserUtil;
import jakarta.ws.rs.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LibraryService libraryService;

    public User save(User user){
        return userRepository.save(user);
    }

    public User postUser(UserDto addUserRequest) {

        validateUserPassword(addUserRequest.getPassword());
        Library library = libraryService.findById(addUserRequest.getLibrary().getId());

        User user = new User();
        user.setName(addUserRequest.getName());
        user.setSurname(addUserRequest.getSurname());
        user.setEmail(addUserRequest.getEmail());
        user.setUsername(addUserRequest.getUsername());
        user.setPassword(addUserRequest.getPassword());
        user.setRole(Role.valueOf(addUserRequest.getRole().toUpperCase())); // Enum.vauleOf(String name) is a method provided by the Enum class in Java that takes string as input and converts it to corresponding enum constant
        user.setActive(addUserRequest.isActive());
        user.setLibrary(library);
        save(user);
        return user;
    }

    public void validateUserPassword(String userPassword) {
        boolean result = userPassword.matches(UserUtil.PASSWORD_REGEX);
        if (result) {
            throw new BadRequestException(UserUtil.PASSWORD_MESSAGE);
        }
    }
}
