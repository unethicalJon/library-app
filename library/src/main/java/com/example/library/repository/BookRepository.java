package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT b.id, b.title, b.author, b.genre, b.section, b.price, b.year_of_publication " +
            "FROM book b " +
            "JOIN library_book lb ON b.id = lb.book_id " +
            "JOIN library l ON lb.library_id = l.id " +
            "JOIN \"user\" u ON u.library_id = l.id " +
            "WHERE u.id = :userId",
            nativeQuery = true)
    List<Book> findBooksByUserLibrary(@Param("userId") Long userId);

}
