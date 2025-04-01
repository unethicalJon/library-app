package com.example.library.dto.library;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LibraryDto {

    private Long id;

    @NotBlank(message = "name is required!")
    private String name;

    @NotBlank(message = "name is required!")
    private String address;

}
