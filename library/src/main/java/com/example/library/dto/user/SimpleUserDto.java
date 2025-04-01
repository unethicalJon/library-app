package com.example.library.dto.user;

import com.example.library.entity.User;
import lombok.Data;

@Data
public class SimpleUserDto {

    private Long id;

    private String name;

    private String surname;

    public static SimpleUserDto convertToSimpleUserDto(User user) {
        SimpleUserDto dto = new SimpleUserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        return dto;
    }
}
