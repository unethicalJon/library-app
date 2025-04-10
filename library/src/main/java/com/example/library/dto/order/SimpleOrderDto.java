package com.example.library.dto.order;

import com.example.library.datatype.Status;
import com.example.library.dto.bookorder.SimpleBookOrderDto;
import com.example.library.dto.user.SimpleUserDto;
import com.example.library.entity.Order;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SimpleOrderDto {

    private Long id;

    private Status status;

    private int year;

    private String userNote;

    private String adminNote;

    private SimpleUserDto user;

    private List<SimpleBookOrderDto> bookOrders;

    public static SimpleOrderDto convertToSimpleOrderDto(Order order) {
        SimpleOrderDto dto = new SimpleOrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setYear(order.getYear());
        dto.setUserNote(order.getUserNote() != null ? order.getUserNote() : " ");
        dto.setAdminNote(order.getAdminNote() != null ? order.getAdminNote() : " ");
        dto.setUser(SimpleUserDto.convertToSimpleUserDto(order.getUser()));

        List<SimpleBookOrderDto> bookOrderDtos = order.getBookOrders().stream()
                .map(SimpleBookOrderDto::convertToSimpleBookOrderDto)
                .collect(Collectors.toList());

        dto.setBookOrders(bookOrderDtos);
        return dto;
    }
}
