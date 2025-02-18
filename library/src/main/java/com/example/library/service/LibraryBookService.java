package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.Library;
import com.example.library.entity.LibraryBook;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LibraryBookRepository;
import com.example.library.repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LibraryBookService {

    private final LibraryBookRepository libraryBookRepository;
    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    @Transactional
    public LibraryBook updateStock(Long libraryId, Long bookId, int additionalStock) {
        Library library = getLibraryById(libraryId);
        Book book = getBookById(bookId);

        LibraryBook libraryBook = libraryBookRepository.findByLibraryAndBook(library, book)
                .orElse(null);

        if (libraryBook != null) {
            // Book already exists in the library, update stock
            libraryBook.setStock(libraryBook.getStock() + additionalStock);
        } else {
            // Book doesn't exist in the library, create a new entry
            libraryBook = new LibraryBook();
            libraryBook.setLibrary(library);
            libraryBook.setBook(book);
            libraryBook.setStock(additionalStock);
        }

        return saveLibraryBook(libraryBook);
    }

    private Library getLibraryById(Long libraryId) {
        return libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library not found"));
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    private LibraryBook saveLibraryBook(LibraryBook libraryBook) {
        return libraryBookRepository.save(libraryBook);
    }
}
