package com.example.library.dto.bookorder;

import com.example.library.dto.book.SimpleBookDto;
import com.example.library.entity.BookOrder;
import lombok.Data;

@Data
public class SimpleBookOrderDto {

    private SimpleBookDto book;

    private int size;

    public static SimpleBookOrderDto convertToSimpleBookOrderDto(BookOrder bookOrder) {
        SimpleBookOrderDto dto = new SimpleBookOrderDto();
        dto.setBook(SimpleBookDto.convertToSimpleBookDto(bookOrder.getBook()));
        dto.setSize(bookOrder.getSize());
        return dto;
    }

}
