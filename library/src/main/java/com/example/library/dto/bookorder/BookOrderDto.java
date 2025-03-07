package com.example.library.dto.bookorder;

import com.example.library.dto.book.BookDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookOrderDto {

    private Long id;

    private BookDto book;

    @NotBlank(message = "size is required")
    private int size;

}
