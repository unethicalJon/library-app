package com.example.library.repository;

import com.example.library.entity.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {

    @Query("Select bo.id from BookOrder bo where bo.order.id = ?1" )
    Long getBoFromOrder(Long orderId);
}
