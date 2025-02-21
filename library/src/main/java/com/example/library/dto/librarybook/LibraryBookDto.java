package com.example.library.dto.librarybook;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class LibraryBookDto {

    private Long id;

    private Long libraryId;

    private Long bookId;

    @NotNull(message = "stock is required")
    @PositiveOrZero(message = "stock can't be less than 1")
    private Integer stock;
}
