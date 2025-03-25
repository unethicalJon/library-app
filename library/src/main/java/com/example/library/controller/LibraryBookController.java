package com.example.library.controller;

import com.example.library.dto.librarybook.LibraryBookDto;
import com.example.library.entity.LibraryBook;
import com.example.library.service.LibraryBookService;
import com.example.library.util.apiDocs.LibraryBookControllerDoc;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.LibraryBookController.BASE)
@RequiredArgsConstructor
public class LibraryBookController {

    private final LibraryBookService libraryBookService;

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PutMapping(RestConstants.LibraryBookController.UPDATE_STOCK)
    @LibraryBookControllerDoc.UpdateStockDoc
    public ResponseEntity<List<Long>> updateStock(
            @PathVariable(value = RestConstants.ID) Long libraryId,
            @RequestBody List<LibraryBookDto> libraryBookDtos) {

        List<Long> updatedLibraryBooks = libraryBookService.updateStock(libraryId, libraryBookDtos);
        return ResponseEntity.ok(updatedLibraryBooks);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @GetMapping()
    @LibraryBookControllerDoc.GetLibraryBooksDoc
    public Page<LibraryBookDto> getLibraryBooks(@RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return libraryBookService.getLibraryBooks(page, size);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @GetMapping(RestConstants.LibraryBookController.AVAILABLE_BOOKS)
    @LibraryBookControllerDoc.GetAvailableBooksDoc
    public Page<LibraryBook> getAvailableBooks(@RequestParam(value = "library", required = false) Long libraryId,
                                               @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
                                               @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return libraryBookService.getAvailableBooks(libraryId, page, size);
    }
}
