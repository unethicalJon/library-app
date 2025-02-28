package com.example.library.service;

import com.example.library.datatype.Status;
import com.example.library.dto.bookorder.BookOrderDto;
import com.example.library.dto.order.OrderDto;
import com.example.library.entity.*;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.BookOrderRepository;
import com.example.library.repository.LibraryBookRepository;
import com.example.library.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final BookService bookService;
    private final LibraryBookService libraryBookService;
    private final BookOrderRepository bookOrderRepository;
    private final LibraryBookRepository libraryBookRepository;

    public Order findById(Long id) { return orderRepository.findById(id).orElseThrow(() -> new BadRequestException("Order not found for id: " + id));}

    public BookOrder findBookOrderById(Long id) { return bookOrderRepository.findById(id).orElseThrow(() -> new BadRequestException("BookOrder not found for id: " + id));}


    private Integer generateOrderNumber() {
        return Math.abs(UUID.randomUUID().toString().hashCode());
    }

    public Boolean validateBookStock(Book book, BookOrderDto bookOrderDto) {
        Integer bookStock = libraryBookService.getStockForBook(book);
        if (bookStock < bookOrderDto.getSize()) {
            throw new IllegalArgumentException("There is not enough stock for book: " + bookOrderDto.getBookId());
        } else {
            return true;
        }
    }

    public Order createOrder(OrderDto orderDto) {
        User user = userService.loggedInUser();
        Order order = new Order();

        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setUserNote(orderDto.getNote());
        return orderRepository.save(order);
    }

    @Transactional(rollbackOn = Exception.class)
    public List<BookOrder> createBookOrder(OrderDto orderDto, List<BookOrderDto> bookOrderDtos) {

        Order order = createOrder(orderDto);

        List<BookOrder> bookOrders = new ArrayList<>();

        if (bookOrderDtos.isEmpty()) {
            throw new IllegalArgumentException("Book list cannot be empty");
        }

        for (BookOrderDto bookOrderDto : bookOrderDtos) {

            Book book = bookService.findById(bookOrderDto.getBookId());

            BookOrder bookOrder = new BookOrder();
            bookOrder.setOrder(order);
            bookOrder.setBook(book);
            bookOrder.setSize(bookOrderDto.getSize());
            bookOrder.setValue((int) (book.getPrice() * bookOrderDto.getSize()));
            //Validate quantity
            validateBookStock(book, bookOrderDto);
            bookOrder = bookOrderRepository.save(bookOrder);
            bookOrders.add(bookOrder);
            }
        return bookOrders;
    }


    public Order updateOrder(Long orderId, OrderDto orderDto) {
        Order order = findById(orderId);

        order.setStatus(Status.valueOf(orderDto.getStatus().toUpperCase()));

        BookOrder bookOrder = findBookOrderById(bookOrderRepository.getBoFromOrder(order.getId()));
        Book book = bookService.findById(bookOrder.getBook().getId());
        LibraryBook libraryBook = libraryBookService.getLbByBook(book);

        switch (order.getStatus()) {
            case PENDING -> throw new IllegalStateException("You have to choose between Refused and Accepted!");
            case REFUSED -> order.setAdminNote(orderDto.getNote());
            case ACCEPTED ->  libraryBook.setStock(libraryBook.getStock() - bookOrder.getSize());
        }
        return orderRepository.save(order);
    }

}
