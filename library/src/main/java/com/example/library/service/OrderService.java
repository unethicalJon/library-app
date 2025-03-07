package com.example.library.service;

import com.example.library.datatype.Status;
import com.example.library.dto.book.BookDto;
import com.example.library.dto.bookorder.BookOrderDto;
import com.example.library.dto.librarybook.LibraryBookDto;
import com.example.library.dto.order.OrderDto;
import com.example.library.dto.request.SimpleRequestDto;
import com.example.library.entity.*;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.BookOrderRepository;
import com.example.library.repository.OrderRepository;
import com.example.library.repository.UserRepository;
import com.example.library.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.library.security.UserUtil.getLoggedInUser;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final BookService bookService;
    private final LibraryBookService libraryBookService;
    private final BookOrderRepository bookOrderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new BadRequestException("Order not found for id: " + id));
    }

    public Integer getLatestOrderNumber() {
        return orderRepository.findLatestOrderNumber();
    }

    public void validateUser(Order order, User user) {
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Logged-in User doesn't have access in this operation!");
        }
    }

    private void validateStatus(Order order, Status status) {
        if (!order.getStatus().equals(status)) {
            throw new IllegalArgumentException(String.format("Invalid action for order with status %s", status));
        }
    }

    public Order createOrder(OrderDto orderDto) {
        User user = userService.loggedInUser();
        Order order = new Order();

        order.setOrderNumber(getLatestOrderNumber() != null ? getLatestOrderNumber() + 1 : 1);
        order.setUser(user);
        order.setStatus(Status.CREATED);
        order.setUserNote(orderDto.getNote());
        return save(order);
    }

    @Transactional(rollbackOn = Exception.class)
    public List<BookOrder> createBookOrder(OrderDto orderDto) {
        Order order = createOrder(orderDto);
        List<BookOrder> newBookOrder = new ArrayList<>();
        List<BookOrderDto> bookOrderDtoList = orderDto.getBookOrderDtos();
        validateInput(bookOrderDtoList);

        for (BookOrderDto bookOrderDto : bookOrderDtoList) {
            Book book = bookService.findById(bookOrderDto.getBook().getId());
            if (book != null) {
                BookOrder bookOrder = saveBookOrder(order, book, bookOrderDto);
                newBookOrder.add(bookOrder);
            } else {
                throw new IllegalArgumentException("Book can't be null!");
            }
        }

        return newBookOrder;
    }

    private void validateInput(List<BookOrderDto> bookOrderDtos) {
        if (bookOrderDtos.isEmpty()) {
            throw new IllegalArgumentException("Book Order list cannot be empty");
        }
    }

    public BookOrder saveBookOrder(Order order, Book book, BookOrderDto bookOrderDto) {
        BookOrder bookOrder = new BookOrder();
        bookOrder.setOrder(order);
        bookOrder.setBook(book);
        bookOrder.setSize(bookOrderDto.getSize());
        bookOrder.setValue((int) ((book.getPrice() != null ? book.getPrice() : 0.0) * bookOrderDto.getSize()));
        validateBookStock(book, bookOrderDto);
        bookOrder = bookOrderRepository.save(bookOrder);
        return bookOrder;
    }

    private void validateBookStock(Book book, BookOrderDto bookOrderDto) {
        LibraryBook libraryBook = libraryBookService.getLibraryBookByBook(book);
        if (libraryBook.getStock() < bookOrderDto.getSize()) {
            throw new IllegalArgumentException("There is not enough stock for book: " + bookOrderDto.getBook().getId());
        }
    }

    public Order sendForApproval(Long orderId) {
        Order order = findById(orderId);
        User user = userService.loggedInUser();
        validateUser(order, user);
        validateStatus(order, Status.CREATED);

        order.setStatus(Status.PENDING);
        return save(order);

    }

    public Order approveOrder(Long orderId, OrderDto orderDto) {
        Order order = findById(orderId);
        validateStatus(order, Status.PENDING);

        order.setStatus(orderDto.getStatus());
        List<BookOrder> bookOrders = bookOrderRepository.getBookOrdersByOrder(order);

        switch (order.getStatus()) {
            case REFUSED -> order.setAdminNote(orderDto.getNote());
            case ACCEPTED -> {
                for (BookOrder bookOrder : bookOrders) {
                    LibraryBook libraryBook = libraryBookService.getLibraryBookByBookAndLibrary(bookOrder.getBook(), order.getUser().getLibrary());
                    libraryBook.setStock(libraryBook.getStock() - bookOrder.getSize());
                }
            }
        }
        return save(order);
    }

    @Transactional(rollbackOn = Exception.class)
    public Order updateOrder(Long orderId, OrderDto orderDto) {
        Order order = findById(orderId);
        User user = userService.loggedInUser();
        validateUser(order, user);
        validateStatus(order, Status.CREATED);

        order.setUserNote(orderDto.getNote());

        List<BookOrderDto> bookOrderDtoList = orderDto.getBookOrderDtos();
        validateInput(bookOrderDtoList);

        List<Long> incomingBookOrderIds = new ArrayList<>();

        for (BookOrderDto bookOrderDto : bookOrderDtoList) {
            if (bookOrderDto.getBook().getId() != null) {
                Book book = bookService.findById(bookOrderDto.getBook().getId());
                Optional<BookOrder> bookOrder = bookOrderRepository.findByBookAndOrder(book, order);
                if (bookOrder.isPresent()) {
                    updateExistingBookOrder(bookOrder.get(), bookOrderDto, book, incomingBookOrderIds);
                } else {
                    BookOrder newBookOrder = saveBookOrder(order, book, bookOrderDto);
                    incomingBookOrderIds.add(newBookOrder.getId());
                }
            }
        }
        removeBookOrders(order, incomingBookOrderIds);

        return order;
    }

    private void removeBookOrders(Order order, List<Long> incomingBookOrderIds) {
        List<BookOrder> bookOrders = bookOrderRepository.getBookOrdersByOrder(order);

        for (BookOrder bookOrder : bookOrders) {
            if (!incomingBookOrderIds.contains(bookOrder.getId())) {
                bookOrderRepository.delete(bookOrder);
            }
        }
    }

    private void updateExistingBookOrder(BookOrder bookOrder, BookOrderDto bookOrderDto, Book book, List<Long> incomingBookOrderIds) {
        bookOrder.setSize(bookOrderDto.getSize());
        bookOrder.setValue((int) ((book.getPrice() != null ? book.getPrice() : 0.0) * bookOrderDto.getSize()));
        validateBookStock(book, bookOrderDto);
        bookOrderRepository.save(bookOrder);
        incomingBookOrderIds.add(bookOrder.getId());
    }

}



