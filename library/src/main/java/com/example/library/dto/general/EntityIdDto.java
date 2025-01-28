package com.example.library.dto.general;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityIdDto {

    private Long Id;

    public static EntityIdDto of(Long id) {
        return new EntityIdDto(id);
    }
}
