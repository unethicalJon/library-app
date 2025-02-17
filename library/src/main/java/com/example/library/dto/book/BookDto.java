package com.example.library.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotNull(message = "Genre is required")
    private String genre;

    @NotNull(message = "Section is required")
    private String section;

    @Min(value = 1, message = "Price must be greater than zero")
    private double price;

    private LocalDate yearOfPublication;
}
