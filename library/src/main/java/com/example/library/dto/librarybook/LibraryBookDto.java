package com.example.library.dto.librarybook;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class LibraryBookDto {

    private Long id;

    private Long libraryId;

    private Long bookId;

    @NotNull(message = "stock is required")
    @Positive(message = "stock can't be negative")
    private Integer stock;
}
