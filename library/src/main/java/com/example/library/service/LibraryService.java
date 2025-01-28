package com.example.library.service;

import com.example.library.entity.Library;
import com.example.library.repository.LibraryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public Library findById(Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Library with ID" + id + "is not found"));
    }
}
