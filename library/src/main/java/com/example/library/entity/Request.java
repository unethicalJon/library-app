package com.example.library.entity;

import com.example.library.datatype.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="request_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
}
