package com.example.library.dto.order;

import com.example.library.datatype.Status;
import com.example.library.dto.bookorder.BookOrderDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private String note;

    private Status status;

    private int year;

    @NotEmpty(message = "Book list cannot be empty")
    @Valid
    private List<BookOrderDto> bookOrderDtos;
}
