package com.example.library.dto.bookorder;

import com.example.library.entity.BookOrder;
import lombok.Data;

@Data
public class SimpleBookOrderDto {

    private Long bookId;

    private int size;

    public static SimpleBookOrderDto convertToSimpleBookOrderDto(BookOrder bookOrder) {
        SimpleBookOrderDto dto = new SimpleBookOrderDto();
        dto.setBookId(bookOrder.getBook().getId());
        dto.setSize(bookOrder.getSize());
        return dto;
    }

}
