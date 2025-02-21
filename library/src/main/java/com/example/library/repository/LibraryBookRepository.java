package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.LibraryBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {

    Optional<LibraryBook> findByBook(Book book);

    @Query("SELECT lb FROM LibraryBook lb JOIN FETCH lb.library JOIN FETCH lb.book")
    Page<LibraryBook> findAllWithLibraryAndBook(Pageable pageable);

    Page<LibraryBook> findAll(Pageable pageable);
}
