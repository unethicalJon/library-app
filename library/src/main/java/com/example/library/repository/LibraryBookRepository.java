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

    Page<LibraryBook> findAll(Pageable pageable);

    @Query("SELECT lb.book, lb.stock FROM LibraryBook lb WHERE lb.stock > 0 AND (?1 IS NULL OR lb.library.id = ?1)")
    Page<LibraryBook> findBooksByStock(Long libraryId, Pageable pageable);

}
