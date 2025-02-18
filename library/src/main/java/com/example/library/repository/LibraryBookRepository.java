package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.Library;
import com.example.library.entity.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
    Optional<LibraryBook> findByLibraryAndBook(Library library, Book book);
}
