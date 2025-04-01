package com.example.library.dto.bookorder;

import com.example.library.dto.book.BookDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class BookOrderDto {

    @NotNull(message = "book can't be null")
    private BookDto book;

    @PositiveOrZero(message = "size can't be less than 0")
    private int size;

}
