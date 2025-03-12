package com.example.library.controller;

import com.example.library.dto.book.BookDto;
import com.example.library.dto.general.EntityIdDto;
import com.example.library.entity.Book;
import com.example.library.service.BookService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.BookController.BASE)
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PostMapping(RestConstants.BookController.ADD)
    public ResponseEntity<EntityIdDto> addBook(@RequestBody BookDto bookDto) {
        Book book = bookService.postBook(bookDto);
        return new ResponseEntity<>(EntityIdDto.of(book.getId()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @PutMapping(RestConstants.BookController.UPDATE)
    public ResponseEntity<EntityIdDto> updateBook(@PathVariable(value = RestConstants.ID) Long id, @RequestBody BookDto bookDto) {
        Book book = bookService.updateBook(id, bookDto);
        return new ResponseEntity<>(EntityIdDto.of(book.getId()), HttpStatus.OK);
    }

    @GetMapping(RestConstants.BookController.USER_BOOKS)
    public ResponseEntity<List<Book>> getBooksFromUserLibrary() {
        List<Book> books = bookService.getBooksFromUserLibrary();
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @GetMapping()
    public Page<BookDto> getAllBooks(@RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_NUMBER) int page,
                                     @RequestParam(defaultValue = RestConstants.DEFAULT_PAGE_SIZE) int size) {
        return bookService.getAllBooks(keyword, page, size);
    }
    
    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @DeleteMapping(RestConstants.BookController.DELETE)
    public String deleteBook(@PathVariable(value = RestConstants.ID) Long id) {
        try {
            return bookService.deleteBook(id);
        } catch (DataIntegrityViolationException ex){
            throw new RuntimeException("Libri me id: " + id + " eshte e lidhur me entitete te tjera!");
        }
    }
}
