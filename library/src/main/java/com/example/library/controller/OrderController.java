package com.example.library.controller;

import com.example.library.dto.general.EntityIdDto;
import com.example.library.dto.order.OrderDto;
import com.example.library.dto.order.SimpleOrderDto;
import com.example.library.entity.BookOrder;
import com.example.library.entity.Order;
import com.example.library.export.ExportOrders;
import com.example.library.service.BookOrderService;
import com.example.library.service.OrderService;
import com.example.library.util.apiDocs.OrderControllerDoc;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(RestConstants.OrderController.BASE)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final BookOrderService bookOrderService;
    private final ExportOrders exportOrders;

    @PreAuthorize("hasAnyAuthority(@Role.USER_NAME)")
    @PostMapping(RestConstants.OrderController.CREATE_ORDER)
    @OrderControllerDoc.CreateOrderDoc
    public ResponseEntity<EntityIdDto> createBookOrder(@RequestBody OrderDto orderDto) {
        Order order = orderService.createBookOrder(orderDto);
        return new ResponseEntity<>(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority(@Role.USER_NAME)")
    @PutMapping(RestConstants.OrderController.FOR_APPROVAL)
    @OrderControllerDoc.SendOrderForApprovalDoc
    public ResponseEntity<EntityIdDto> sendOrderForApproval(@PathVariable(value = RestConstants.ID) Long id) {
        Order order = orderService.sendForApproval(id);
        return new ResponseEntity<>(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PutMapping(RestConstants.OrderController.APPROVE_ORDER)
    @OrderControllerDoc.ApproveOrderDoc
    public ResponseEntity<EntityIdDto> approveOrder(@PathVariable(value = RestConstants.ID) Long id,
                                                    @RequestBody OrderDto orderDto) {
        Order order = orderService.approveOrder(id, orderDto);
        return new ResponseEntity<>(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority(@Role.USER_NAME)")
    @PutMapping(RestConstants.OrderController.UPDATE_ORDER)
    @OrderControllerDoc.UpdateOrderDoc
    public ResponseEntity<EntityIdDto> updateOrder(@PathVariable(value = RestConstants.ID) Long id,
                                                   @Validated @RequestBody OrderDto orderDto) {
        Order order = orderService.updateOrder(id, orderDto);
        return new ResponseEntity<>(EntityIdDto.of(order.getId()), HttpStatus.OK);
    }

    @GetMapping()
    @OrderControllerDoc.GetOrdersDoc
    public Page<SimpleOrderDto> getOrders(@RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
                                          @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return orderService.getOrders(page, size);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @GetMapping(RestConstants.OrderController.PENDING)
    @OrderControllerDoc.GetPendingOrdersDoc
    public Page<SimpleOrderDto> getPendingOrders(@RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
                                                 @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return orderService.getPendingOrders(page, size);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @GetMapping(RestConstants.OrderController.EXPORT)
    public ResponseEntity<byte[]> exportOrders() throws IOException {

        List<Order> orders = orderService.getAllOrders();
        List<BookOrder> bookOrders = bookOrderService.getAllBookOrders();

        byte[] excelData = exportOrders.ordersExcel(orders, bookOrders);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx")
                .body(excelData);
    }
}

