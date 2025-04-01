package com.example.library.dto.bookorder;

import com.example.library.dto.book.SimpleBookDto;
import com.example.library.entity.Book;
import com.example.library.entity.BookOrder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Top3BooksDto {

    private int year;

    private SimpleBookDto book;

    private int size;

    public static Top3BooksDto convertToTop3BooksDto(Book book, int size, int year) {
        Top3BooksDto dto = new Top3BooksDto();
        dto.setBook(SimpleBookDto.convertToSimpleBookDto(book));
        dto.setSize(size);
        dto.setYear(year);
        return dto;
    }
}

