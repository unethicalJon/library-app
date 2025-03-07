package com.example.library.repository;

import com.example.library.entity.Order;
import com.example.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT MAX(o.OrderNumber) FROM Order o")
    Integer findLatestOrderNumber();

}
