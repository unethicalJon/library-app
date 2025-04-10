package com.example.library.service;

import com.example.library.datatype.Role;
import com.example.library.dto.auth.AuthenticationRequestDto;
import com.example.library.dto.user.PasswordRequestDto;
import com.example.library.dto.user.UserDto;
import com.example.library.dto.user.UserDetailsDto;
import com.example.library.entity.Library;
import com.example.library.entity.User;
import com.example.library.exceptions.BadRequestException;
import com.example.library.exceptions.NotFoundException;
import com.example.library.exceptions.UnauthorizedException;
import com.example.library.repository.UserRepository;
import com.example.library.security.CustomUserDetailService;
import com.example.library.security.CustomUserDetails;
import com.example.library.security.UserUtil;
import com.example.library.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.library.security.UserUtil.getLoggedInUser;

@Service
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LibraryService libraryService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    private final EmailService emailService;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found for ID: " + id));
    }

    public User loggedInUser() {
        CustomUserDetails loggedInUser = getLoggedInUser();
        return userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    public void validateUserPassword(String userPassword) {
        boolean result = userPassword.matches(UserUtil.PASSWORD_REGEX);
        if (!result) {
            throw new BadRequestException(UserUtil.PASSWORD_MESSAGE);
        }
    }

    private void validateNewUsername(String newUsername) {
        if (userRepository.existsByUsername(newUsername)) {
            throw new BadRequestException("Username is already taken");
        }
    }

    private void validateUpdatedUsername(String currentUsername, String newUsername) {
        if (!currentUsername.equals(newUsername) && userRepository.existsByUsername(newUsername)) {
            throw new BadRequestException("Username is already taken");
        }
    }

    public void addUser (User user, UserDto userDto) {
        // Get Libraries
        Library library = libraryService.findById(userDto.getLibrary().getId());
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        user.setActive(false);
        user.setLibrary(library);
    }

    public void updateUser(User user, UserDetailsDto simpleUserDto) {
        user.setName(simpleUserDto.getName());
        user.setSurname(simpleUserDto.getSurname());
        user.setEmail(simpleUserDto.getEmail());
        user.setUsername(simpleUserDto.getUsername());
        // BCrypt encode password
        user.setPassword(passwordEncoder.encode(simpleUserDto.getPassword()));
    }

    public User postUser(UserDto addUserRequest) {
        // New User Info
        User user = new User();
        // Validate the username before adding new user
        validateNewUsername(addUserRequest.getUsername());
        // Validate password
        validateUserPassword(addUserRequest.getPassword());
        addUser(user, addUserRequest);
        return save(user);
    }

    public UserDetailsDto getUserProfile(Long id) {
        User user = findUserById(id);
        // Convert the User entity to a SimpleUserDto
        return modelMapper.map(user, UserDetailsDto.class);
    }

    public User updateUserProfile(Long id, UserDetailsDto updateUser) {
        // findUserById method implementation
        User user = findUserById(id);
        // Validate the username before updating
        validateUpdatedUsername(user.getUsername(), updateUser.getUsername());
        // Validate password
        validateUserPassword(updateUser.getPassword());
        updateUser(user, updateUser);
        return save(user);
    }

    public User updateUserPassword (Long id, PasswordRequestDto passwordRequestDto) {
        User user = findUserById(id);
        if (!passwordRequestDto.getNewPassword().equals(passwordRequestDto.getConfirmationPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        validateUserPassword(passwordRequestDto.getConfirmationPassword());
        user.setPassword(passwordEncoder.encode(passwordRequestDto.getConfirmationPassword()));
        return save(user);
    }

    public void adminActivateUser(Long id) {
        User user = findUserById(id);
        user.setActive(true);
        save(user);
        emailService.sendActivationEmail(user);
    }

    public String createJwtToken(AuthenticationRequestDto authenticationRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword())
            );

        } catch (DisabledException ex) {
            throw new UnauthorizedException("This user is not active!");

        } catch (AuthenticationException e) {
            throw new NotFoundException("Username or password is not correct!");
        }

        UserDetails userDetails = customUserDetailService.loadUserByUsername(authenticationRequestDto.getUsername());

        return jwtUtil.generateToken(userDetails);
    }
}
