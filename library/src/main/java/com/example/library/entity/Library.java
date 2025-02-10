package com.example.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="library")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message = "name is required!")
    @Column(name="name")
    private String name;

    @NotBlank(message = "address is required!")
    @Column(name="address")
    private String address;

    @OneToMany(mappedBy = "library", fetch = FetchType.LAZY)
    private List<User> users;
}
