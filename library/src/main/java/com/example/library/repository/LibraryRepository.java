package com.example.library.repository;

import com.example.library.entity.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    boolean existsByName(String name);

    @Query(value = "SELECT * FROM customers WHERE (:keyword IS NULL OR :keyword = '' OR name LIKE %:keyword% OR address LIKE %:keyword%)",
            nativeQuery = true)
    Page<Library> findbyNameorAddress(@Param("keyword")String keyword, Pageable pageable);
}
