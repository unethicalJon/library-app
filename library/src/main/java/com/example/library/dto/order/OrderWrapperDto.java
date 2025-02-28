package com.example.library.dto.order;

import com.example.library.dto.bookorder.BookOrderDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderWrapperDto {

    private OrderDto orderDto;
    private List<BookOrderDto> bookOrderDtos;

}
