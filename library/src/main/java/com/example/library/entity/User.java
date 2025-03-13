package com.example.library.entity;

import com.example.library.datatype.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message = "name is required!")
    @Column(name="name")
    private String name;

    @NotBlank(message = "surname is required!")
    @Column(name="surname")
    private String surname;

    @NotBlank(message = "email is required!")
    @Email(message = "email is not in the right format!")
    @Column(name="email")
    private String email;

    @NotBlank(message = "username is required!")
    @Column(name="username")
    private String username;

    @NotBlank(message = "password is required!")
    @Column(name="password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role;

    @Column(name="active")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="library_id")
    private Library library;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
}
