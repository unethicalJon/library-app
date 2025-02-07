package com.example.library.repository;

import com.example.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    boolean existsByName(String name);
}
