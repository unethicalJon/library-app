package com.example.library.dto.order;

import com.example.library.datatype.Status;
import com.example.library.dto.bookorder.BookOrderDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private String note;

    private Status status;

    @NotNull(message = "Book list cannot be empty")
    private List<BookOrderDto> bookOrderDtos;

}
