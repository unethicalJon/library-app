package com.example.library.dto.library;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SimpleLibraryDto {

    @NotBlank(message = "name is required!")
    private String name;

    @NotBlank(message = "address is required!")
    private String address;

}
