package com.example.library.controller;

import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.order.OrderDto;
import com.example.library.dto.order.SimpleOrderDto;
import com.example.library.entity.Order;
import com.example.library.service.OrderService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(RestConstants.OrderController.BASE)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyAuthority(@Role.USER_NAME)")
    @PostMapping(RestConstants.OrderController.CREATE_ORDER)
    public ResponseEntity<EntityIdDto> createBookOrder(
            @RequestBody OrderDto orderDto) {

        Order order = orderService.createBookOrder(orderDto);
        return new ResponseEntity<>(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority(@Role.USER_NAME)")
    @PutMapping(RestConstants.OrderController.FOR_APPROVAL)
    public ResponseEntity<EntityIdDto> sendOrderForApproval(
            @PathVariable(value = RestConstants.ID) Long id) {

        Order order = orderService.sendForApproval(id);
        return new ResponseEntity<>(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PutMapping(RestConstants.OrderController.APPROVE_ORDER)
    public ResponseEntity<EntityIdDto> approveOrder(@PathVariable(value = RestConstants.ID) Long id,
                                             @RequestBody OrderDto orderDto) {
        Order order = orderService.approveOrder(id, orderDto);
        return new ResponseEntity<>(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority(@Role.USER_NAME)")
    @PutMapping(RestConstants.OrderController.UPDATE_ORDER)
    public ResponseEntity<EntityIdDto> updateOrder(@PathVariable(value = RestConstants.ID) Long id,
                                             @Validated @RequestBody OrderDto orderDto) {
        Order order = orderService.updateOrder(id, orderDto);
        return new ResponseEntity<>(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }

    @GetMapping()
    public Page<SimpleOrderDto> getOrders(
            @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return orderService.getOrders(page, size);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @GetMapping(RestConstants.OrderController.PENDING)
    public Page<SimpleOrderDto> getPendingOrders(
            @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return orderService.getPendingOrders(page, size);
    }
}

