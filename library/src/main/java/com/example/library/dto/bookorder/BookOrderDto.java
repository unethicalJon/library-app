package com.example.library.dto.bookorder;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookOrderDto {

    private Long bookId;

    @NotBlank(message = "size is required")
    private int size;

}
