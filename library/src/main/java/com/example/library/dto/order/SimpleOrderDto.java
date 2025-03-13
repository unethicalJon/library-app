package com.example.library.dto.order;

import com.example.library.datatype.Status;
import com.example.library.dto.bookorder.SimpleBookOrderDto;
import com.example.library.entity.Order;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SimpleOrderDto {

    private Long id;

    private Status status;

    private String userNote;

    private String adminNote;

    private Long userId;

    private List<SimpleBookOrderDto> bookOrders;

    public static SimpleOrderDto convertToSimpleOrderDto(Order order) {
        SimpleOrderDto dto = new SimpleOrderDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setUserNote(order.getUserNote() != null ? order.getUserNote() : " ");
        dto.setAdminNote(order.getAdminNote() != null ? order.getAdminNote() : " ");
        dto.setUserId(order.getUser().getId());

        List<SimpleBookOrderDto> bookOrderDtos = order.getBookOrders().stream()
                .map(SimpleBookOrderDto::convertToSimpleBookOrderDto)
                .collect(Collectors.toList());

        dto.setBookOrders(bookOrderDtos);
        return dto;
    }

}
