package com.example.library.service;

import com.example.library.dto.librarybook.LibraryBookDto;
import com.example.library.entity.Book;
import com.example.library.entity.Library;
import com.example.library.entity.LibraryBook;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.LibraryBookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LibraryBookService {

    private final LibraryBookRepository libraryBookRepository;
    private final LibraryService libraryService;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    public LibraryBook save(LibraryBook libraryBook) {return libraryBookRepository.save(libraryBook);}

    public LibraryBook findById(Long id) {
        return libraryBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LibraryBook with ID " + id + " is not found"));
    }

    private LibraryBook createNewLibraryBook(Library library, Book book, LibraryBookDto libraryBookDto) {
        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setLibrary(library);
        libraryBook.setBook(book);
        libraryBook.setStock(libraryBookDto.getStock());
        return save(libraryBook);
    }

    private LibraryBook updateExistingLibraryBook(LibraryBookDto libraryBookDto) {
        LibraryBook libraryBook = findById(libraryBookDto.getId());
        libraryBook.setStock(libraryBookDto.getStock());
        return save(libraryBook);
    }

    private void validateInput(List<LibraryBookDto> libraryBookDtos) {
        if (libraryBookDtos.isEmpty()) {
            throw new IllegalArgumentException("Book list cannot be empty");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public List<Long> updateStock(Long libraryId, List<LibraryBookDto> libraryBookDtos) {
        validateInput(libraryBookDtos);
        Library library = libraryService.findById(libraryId);
        List<Long> updatedLibraryBooks = new ArrayList<>();
        handleUpdateStock(libraryId, libraryBookDtos, library, updatedLibraryBooks);
        return updatedLibraryBooks;
    }

    private void handleUpdateStock(Long libraryId, List<LibraryBookDto> libraryBookDtos, Library library, List<Long> updatedLibraryBooks) {
        for (LibraryBookDto libraryBookDto : libraryBookDtos) {
            if (libraryBookDto.getId() != null && updatedLibraryBooks.contains(libraryBookDto.getId())) {
                continue; // Skip duplicate updates
            }
            Book book = bookService.findById(libraryBookDto.getBookId());
            LibraryBook libraryBook = libraryBookDto.getId() == null
                    ? handleNewLibraryBook(library, book, libraryBookDto)
                    : handleExistingLibraryBook(libraryId, libraryBookDto);

            updatedLibraryBooks.add(libraryBook.getId());
        }
    }

    private LibraryBook handleNewLibraryBook(Library library, Book book, LibraryBookDto libraryBookDto) {
        libraryBookRepository.findByLibraryAndBook(library, book)
                .ifPresent(existingBook -> {
                    throw new BadRequestException("Book: " + libraryBookDto.getBookId() + " is a duplicate");
                });
        return createNewLibraryBook(library, book, libraryBookDto);
    }

    private LibraryBook handleExistingLibraryBook(Long libraryId, LibraryBookDto libraryBookDto) {
        LibraryBook libraryBook = findById(libraryBookDto.getId());

        if (!Objects.equals(libraryBook.getLibrary().getId(), libraryId)) {
            throw new BadRequestException("Library IDs are not matching");
        }
        return updateExistingLibraryBook(libraryBookDto);
    }

    public Page<LibraryBookDto> getLibraryBooks(int page, int size) {
        return libraryBookRepository.findAll(PageRequest.of(page, size)).map(this::mapToDto);
    }

    private LibraryBookDto mapToDto(LibraryBook libraryBook) {
        return modelMapper.map(libraryBook, LibraryBookDto.class);
    }

    public Page<LibraryBook> getAvailableBooks(Long libraryId, int page, int size) {
        return libraryBookRepository.findBooksByStock(libraryId, PageRequest.of(page, size));
    }

    public LibraryBook getLibraryBookByBookAndLibrary(Book book, Library library) {
        return libraryBookRepository.findLibraryBookByBookAndLibrary(book, library);
    }

}
