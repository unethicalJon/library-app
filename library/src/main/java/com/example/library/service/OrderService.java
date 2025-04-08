package com.example.library.service;

import com.example.library.datatype.Status;
import com.example.library.dto.bookorder.BookOrderDto;
import com.example.library.dto.order.OrderDto;
import com.example.library.dto.order.SimpleOrderDto;
import com.example.library.entity.*;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.BookOrderRepository;
import com.example.library.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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
            throw new BadRequestException(String.format("Invalid action for order with status %s", status));
        }
    }

    public Order createOrder(OrderDto orderDto) {
        User user = userService.loggedInUser();
        Order order = new Order();

        order.setOrderNumber(getLatestOrderNumber() != null ? getLatestOrderNumber() + 1 : 1);
        order.setUser(user);
        order.setStatus(Status.CREATED);
        order.setYear(LocalDate.now().getYear());
        order.setUserNote(orderDto.getNote());
        return save(order);
    }

    @Transactional(rollbackOn = Exception.class)
    public Order createBookOrder(OrderDto orderDto) {
        Order order = createOrder(orderDto);
        List<BookOrderDto> bookOrderDtoList = orderDto.getBookOrderDtos();

        for (BookOrderDto bookOrderDto : bookOrderDtoList) {
            Book book = bookService.findById(bookOrderDto.getBook().getId());
            if (book != null) {
                saveBookOrder(order, book, bookOrderDto);
            } else {
                throw new BadRequestException("Book can't be null!");
            }
        }
        return order;
    }

    public BookOrder saveBookOrder(Order order, Book book, BookOrderDto bookOrderDto) {
        validateBookStockForPayload(book, order.getUser().getLibrary(), bookOrderDto);
        BookOrder bookOrder = new BookOrder();
        bookOrder.setOrder(order);
        bookOrder.setBook(book);
        bookOrder.setSize(bookOrderDto.getSize());
        bookOrder.setValue((int) ((book.getPrice() != null ? book.getPrice() : 1.0) * bookOrderDto.getSize()));
        bookOrder = bookOrderRepository.save(bookOrder);
        return bookOrder;
    }

    private LibraryBook checkAndGetLibraryBookByBookAndLibrary(Book book, Library library) {
        LibraryBook libraryBook = libraryBookService.getLibraryBookByBookAndLibrary(book, library);
        if (libraryBook == null) {
            throw new BadRequestException("No copy of book: " + book.getId() + " found in library");
        }
        return libraryBook;
    }

    private void validateBookStockForPayload(Book book, Library library, BookOrderDto bookOrderDto) {
        LibraryBook libraryBook = checkAndGetLibraryBookByBookAndLibrary(book, library);
            if (libraryBook.getStock() < bookOrderDto.getSize()) {
                throw new BadRequestException("There is not enough stock for book: " + bookOrderDto.getBook().getTitle());
        }
    }

    private void validateBookStockForDatabase(Book book, Library library, BookOrder bookOrder) {
        LibraryBook libraryBook = checkAndGetLibraryBookByBookAndLibrary(book, library);
        if (libraryBook.getStock() < bookOrder.getSize()) {
            throw new BadRequestException("There is not enough stock for book: " + bookOrder.getBook().getTitle());
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

    @Transactional(rollbackOn = Exception.class)
    public Order approveOrder(Long orderId, OrderDto orderDto) {
        Order order = findById(orderId);
        validateStatus(order, Status.PENDING);

        order.setStatus(orderDto.getStatus());
        List<BookOrder> bookOrders = bookOrderRepository.getBookOrdersByOrder(order);

        switch (order.getStatus()) {
            case REFUSED -> order.setAdminNote(orderDto.getNote());
            case ACCEPTED -> {
                for (BookOrder bookOrder : bookOrders) {
                    validateBookStockForDatabase(bookOrder.getBook(), order.getUser().getLibrary(), bookOrder);
                    LibraryBook libraryBook = libraryBookService.getLibraryBookByBookAndLibrary(bookOrder.getBook(), order.getUser().getLibrary());
                    libraryBook.setStock(libraryBook.getStock() - bookOrder.getSize());
                }
            }
        }
        return save(order);
    }

    @Transactional(rollbackOn = Exception.class)
    public Order updateOrder(Long orderId, OrderDto orderDto) {
        List<Long> incomingBookOrderIds = new ArrayList<>();
        Order order = findById(orderId);
        User user = userService.loggedInUser();
        validateUser(order, user);
        validateStatus(order, Status.CREATED);

        order.setUserNote(orderDto.getNote());
        updateBookOrders(orderDto.getBookOrderDtos(), order, incomingBookOrderIds);
        removeBookOrders(order, incomingBookOrderIds);

        return order;
    }

    private void updateBookOrders(List<BookOrderDto> bookOrderDtoList, Order order, List<Long> incomingBookOrderIds) {
        Set<Long> processedBookIds = new HashSet<>();
        for (BookOrderDto bookOrderDto : bookOrderDtoList) {
            if (bookOrderDto.getBook().getId() != null) {
                validateBookDuplicates(bookOrderDto.getBook().getId(), processedBookIds);
                Book book = bookService.findById(bookOrderDto.getBook().getId());
                Optional<BookOrder> bookOrder = bookOrderRepository.findByBookAndOrder(book, order);
                if (bookOrder.isPresent()) {
                    updateExistingBookOrder(bookOrder.get(), bookOrderDto, book, order.getUser().getLibrary(), incomingBookOrderIds);
                } else {
                    BookOrder newBookOrder = saveBookOrder(order, book, bookOrderDto);
                    incomingBookOrderIds.add(newBookOrder.getId());
                }
            } else {
                throw new BadRequestException("Book id can't be null!");
            }
        }
    }

    private void removeBookOrders(Order order, List<Long> incomingBookOrderIds) {
        List<BookOrder> bookOrders = bookOrderRepository.getBookOrdersByOrder(order);

        for (BookOrder bookOrder : bookOrders) {
            if (!incomingBookOrderIds.contains(bookOrder.getId())) {
                bookOrderRepository.delete(bookOrder);
            }
        }
    }

    private void updateExistingBookOrder(BookOrder bookOrder, BookOrderDto bookOrderDto, Book book, Library library, List<Long> incomingBookOrderIds) {
        validateBookStockForPayload(book, library, bookOrderDto);
        bookOrder.setSize(bookOrderDto.getSize());
        bookOrder.setValue((int) ((book.getPrice() != null ? book.getPrice() : 1.0) * bookOrderDto.getSize()));
        bookOrderRepository.save(bookOrder);
        incomingBookOrderIds.add(bookOrder.getId());
    }

    private void validateBookDuplicates(Long bookId, Set<Long> processedBookIds){
        if (processedBookIds.contains(bookId)) {
            throw new BadRequestException("Duplicate books detected: " + bookId);
        }
        processedBookIds.add(bookId);
    }

    public Page<SimpleOrderDto> getOrders(int page, int size) {
        User user = userService.loggedInUser();
        Page<Order> orders = switch (user.getRole()) {
            case ADMIN -> orderRepository.findAll(PageRequest.of(page, size));
            case USER -> orderRepository.findByUser(user, PageRequest.of(page, size));
        };
        return orders.map(SimpleOrderDto::convertToSimpleOrderDto);
    }

    public Page<SimpleOrderDto> getPendingOrders(int page, int size) {
        Page<Order> orders = orderRepository.findByStatus(Status.PENDING, PageRequest.of(page, size));
        return orders.map(SimpleOrderDto::convertToSimpleOrderDto);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByIdAsc();
    }
}



