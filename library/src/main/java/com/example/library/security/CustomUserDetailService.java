package com.example.library.security;

import com.example.library.entity.User;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("Username not found"));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " is not found!");
        } else {
            CustomUserDetails.UserBuilder builder = CustomUserDetails
                    .username(username)
                    .password(user.getPassword())
                    .authorities(Set.of(new SimpleGrantedAuthority(user.getRole().getName())))
                    .disabled(!user.isActive())
                    .id(user.getId());

            return builder.build();
        }
    } }
