package com.example.library.service;

import com.example.library.dto.librarybook.LibraryBookDto;
import com.example.library.entity.Book;
import com.example.library.entity.Library;
import com.example.library.entity.LibraryBook;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LibraryBookRepository;
import com.example.library.repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryBookService {

    private final LibraryBookRepository libraryBookRepository;
    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    private <T> T findEntityById(Long id, JpaRepository<T, Long> repository, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName + " with id " + id + " not found"));
    }

    private Library findLibraryById(Long id) {
        return findEntityById(id, libraryRepository, "Library");
    }

    private Book findBookById(Long id) {
        return findEntityById(id, bookRepository, "Book");
    }

    private LibraryBook findLibraryBookById(Long id) {
        return findEntityById(id, libraryBookRepository, "LibraryBook");
    }

    private LibraryBook createNewLibraryBook(Library library, Book book, LibraryBookDto libraryBookDto) {
        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setLibrary(library);
        libraryBook.setBook(book);
        libraryBook.setStock(libraryBookDto.getStock());
        return libraryBookRepository.save(libraryBook);
    }

    private LibraryBook updateExistingLibraryBook(LibraryBookDto libraryBookDto) {
        LibraryBook libraryBook = findLibraryBookById(libraryBookDto.getId());
        libraryBook.setStock(libraryBookDto.getStock());
        return libraryBookRepository.save(libraryBook);
    }

    private void validateInput(List<LibraryBookDto> libraryBookDtos) {
        if (libraryBookDtos.isEmpty()) {
            throw new IllegalArgumentException("Book list cannot be empty");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public List<Long> updateStock(Long libraryId, List<LibraryBookDto> libraryBookDtos) {
        validateInput(libraryBookDtos);

        Library library = findLibraryById(libraryId);
        List<Long> updatedLibraryBooks = new ArrayList<>();

        for (LibraryBookDto libraryBookDto : libraryBookDtos) {
            Book book = findBookById(libraryBookDto.getBookId());

            LibraryBook libraryBook;
            if (libraryBookDto.getId() == null) {
                // Check if a LibraryBook already exists for the given library and book
                Optional<LibraryBook> existingBook = libraryBookRepository.findByBook(book);
                if (existingBook.isPresent()) {
                    // Throw an exception if a matching LibraryBook exists
                    throw new IllegalArgumentException("You are trying to add a Book (with id:" + libraryBookDto.getBookId() + "), which already exists in LibraryBook." +
                            "\n If you want to update its' stock, please insert the respective LibraryBookId.");
                }
                // Create new LibraryBook if it doesn't exist
                libraryBook = createNewLibraryBook(library, book, libraryBookDto);
            } else {
                // Check if LibraryBookId's LibraryId is the same as PathVariable LibraryId
                libraryBook = findLibraryBookById(libraryBookDto.getId());
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
}
