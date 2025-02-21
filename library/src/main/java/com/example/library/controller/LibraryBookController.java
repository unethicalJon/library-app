package com.example.library.controller;

import com.example.library.dto.librarybook.LibraryBookDto;
import com.example.library.dto.librarybook.SimpleLibraryBookDto;
import com.example.library.service.LibraryBookService;
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

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping(RestConstants.LibraryBookController.UPDATE_STOCK)
    public ResponseEntity<List<Long>> updateStock(
            @PathVariable(value = RestConstants.ID) Long libraryId,
            @RequestBody List<LibraryBookDto> libraryBookDtos) {

        List<Long> updatedLibraryBooks = libraryBookService.updateStock(libraryId, libraryBookDtos);
        return ResponseEntity.ok(updatedLibraryBooks);
    }

/*    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping()
    public Page<SimpleLibraryBookDto> getAllLibraryBooks(@RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                         @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return libraryBookService.getAllLibraryBooks(page, size);
    }*/

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping()
    public Page<LibraryBookDto> getLibraryBooks(@RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return libraryBookService.getLibraryBooks(page, size);
    }
}
