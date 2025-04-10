package com.example.library.entity;

import com.example.library.datatype.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="order_number", unique = true, nullable = false)
    private Integer OrderNumber;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name="user_note")
    private String userNote;

    @Column(name="admin_note")
    private String adminNote;

    @Column(name="year")
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<BookOrder> bookOrders;
}
