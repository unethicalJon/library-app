package com.example.library.controller;

import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.order.OrderDto;
import com.example.library.entity.BookOrder;
import com.example.library.entity.Order;
import com.example.library.service.OrderService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.OrderController.BASE)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping(RestConstants.OrderController.CREATE_ORDER)
    public ResponseEntity<List<BookOrder>> createBookOrder(
            @RequestBody OrderDto orderDto) {

        List<BookOrder> bookOrders = orderService.createBookOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookOrders);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping(RestConstants.OrderController.FOR_APPROVAL)
    public ResponseEntity<Order> sendOrderForApproval(
            @PathVariable(value = RestConstants.ID) Long id) {

        Order order = orderService.sendForApproval(id);
        return new ResponseEntity(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(RestConstants.OrderController.APPROVE_ORDER)
    public ResponseEntity<Order> approveOrder(@PathVariable(value = RestConstants.ID) Long id,
                                             @RequestBody OrderDto orderDto) {
        Order order = orderService.approveOrder(id, orderDto);
        return new ResponseEntity(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping(RestConstants.OrderController.UPDATE_ORDER)
    public ResponseEntity<Order> updateOrder(@PathVariable(value = RestConstants.ID) Long id,
                                                      @RequestBody OrderDto orderDto) {

        Order order = orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(order);
    }
}

