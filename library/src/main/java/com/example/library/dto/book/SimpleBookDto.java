package com.example.library.dto.book;

import com.example.library.entity.Book;
import lombok.Data;

@Data
public class SimpleBookDto {

    private Long id;

    private String title;

    private String author;

    public static SimpleBookDto convertToSimpleBookDto(Book book) {
        SimpleBookDto dto = new SimpleBookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        return dto;
    }
}
