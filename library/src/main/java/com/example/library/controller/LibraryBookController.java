package com.example.library.controller;

import com.example.library.dto.librarybook.LibraryBookDto;
import com.example.library.entity.LibraryBook;
import com.example.library.service.LibraryBookService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestConstants.LibraryBookController.BASE)
@RequiredArgsConstructor

public class LibraryBookController {

    private final LibraryBookService libraryBookService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(RestConstants.LibraryBookController.UPDATE_STOCK)
    public ResponseEntity<LibraryBook> updateStock(@RequestBody LibraryBookDto request) {
        LibraryBook libraryBook = libraryBookService.updateStock(
                request.getLibraryId(), request.getBookId(), request.getStock());
        return ResponseEntity.ok(libraryBook);
    }
}
