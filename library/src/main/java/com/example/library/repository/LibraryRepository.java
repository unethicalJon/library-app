package com.example.library.repository;

import com.example.library.entity.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    boolean existsByName(String name);

    Page<Library> findByNameContaining(String name, Pageable pageable);

    Page<Library> findAllByNameContainingOrAddressContaining(String name, String address, Pageable pageable);
}
