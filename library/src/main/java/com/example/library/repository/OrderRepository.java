package com.example.library.repository;

import com.example.library.datatype.Status;
import com.example.library.entity.Order;
import com.example.library.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT MAX(o.OrderNumber) FROM Order o")
    Integer findLatestOrderNumber();

    Page<Order> findByUser(User user, Pageable pageable);

    Page<Order> findByStatus(Status status, Pageable pageable);

    @Query("SELECT o FROM Order o ORDER BY o.id ASC")
    List<Order> findAllByIdAsc();
}
