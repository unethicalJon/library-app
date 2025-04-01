package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.BookOrder;
import com.example.library.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {

    List<BookOrder> getBookOrdersByOrder(Order order);

    Optional<BookOrder> findByBookAndOrder(Book book, Order order);

    @Query("SELECT bo.book, SUM(bo.size) " +
            "FROM BookOrder bo " +
            "JOIN bo.order o " +
            "WHERE o.status = 'ACCEPTED' " +
            "AND o.year = ?1 " +
            "GROUP BY bo.book " +
            "ORDER BY SUM(bo.size) DESC")
    List<Object[]> findTopSellingBooks(int year, Pageable pageable);
}
