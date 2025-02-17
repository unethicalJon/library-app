package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT * FROM book WHERE (:keyword IS NULL OR :keyword = '' OR title LIKE %:keyword% OR author LIKE %:keyword%)",
            nativeQuery = true)
    Page<Book> findAllbyTitleOrAuthor(@Param("keyword")String keyword, Pageable pageable);
}

