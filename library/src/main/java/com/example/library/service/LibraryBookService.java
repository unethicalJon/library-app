package com.example.library.service;

import com.example.library.dto.librarybook.LibraryBookDto;
import com.example.library.entity.Book;
import com.example.library.entity.Library;
import com.example.library.entity.LibraryBook;
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
import java.util.Optional;

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

        for (LibraryBookDto libraryBookDto : libraryBookDtos) {
            Book book = bookService.findById(libraryBookDto.getBookId());

            LibraryBook libraryBook;
            if (libraryBookDto.getId() == null) {
                // Check if a LibraryBook already exists for the given library and book
                Optional<LibraryBook> existingBook = libraryBookRepository.findByLibraryAndBook(library, book);
                if (existingBook.isPresent()) {
                    // Throw an exception if a matching LibraryBook exists
                    throw new IllegalArgumentException("You are trying to add a Book (with id:" + libraryBookDto.getBookId() + "), which already exists in LibraryBook." +
                            "\n If you want to update its' stock, please insert the respective LibraryBookId.");
                }
                // Create new LibraryBook if it doesn't exist
                libraryBook = createNewLibraryBook(library, book, libraryBookDto);
            } else {
                // Check if LibraryBookId's LibraryId is the same as PathVariable LibraryId
                libraryBook = findById(libraryBookDto.getId());
                if (!Objects.equals(libraryBook.getLibrary().getId(), libraryId)) {
                    throw new IllegalArgumentException("Library IDs are not matching");
                }
                // Update the existing LibraryBook
                libraryBook = updateExistingLibraryBook(libraryBookDto);
            }
            updatedLibraryBooks.add(libraryBook.getId());
        }
        return updatedLibraryBooks;
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

    public LibraryBook getLibraryBookByBook(Book book) {
        return libraryBookRepository.findLibraryBookByBook(book);
    }
}
