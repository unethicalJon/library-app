package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.BookOrder;
import com.example.library.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {

    List<BookOrder> getBookOrdersByOrder(Order order);

    Optional<BookOrder> findByBookAndOrder(Book book, Order order);
}
