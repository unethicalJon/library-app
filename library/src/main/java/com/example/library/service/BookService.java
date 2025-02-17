package com.example.library.service;

import com.example.library.datatype.Genre;
import com.example.library.datatype.Section;
import com.example.library.dto.book.BookDto;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.exceptions.BadRequestException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import com.example.library.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.library.security.UserUtil.getLoggedInUser;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book with ID " + id + " is not found"));
    }

    public void addBook (Book book, BookDto addBook){
        book.setTitle(addBook.getTitle());
        book.setAuthor(addBook.getAuthor());
        book.setGenre(Genre.valueOf(addBook.getGenre().toUpperCase()));
        book.setSection(Section.valueOf(addBook.getSection().toUpperCase()));
        book.setPrice(addBook.getPrice());
        book.setYearOfPublication(addBook.getYearOfPublication());
    }

    public Book postBook(BookDto bookDto) {
        Book book = new Book();
        addBook(book, bookDto);
        save(book);
        return book;
    }

    public Book updateBook(Long id, BookDto bookDto) {
        Book book = findById(id);
        addBook(book, bookDto);
        save(book);
        return book;
    }

    private User loggedInUser() {
        CustomUserDetails loggedInUser = getLoggedInUser();
        return userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    public List<BookDto> getBooksFromUserLibrary() {
        User user = loggedInUser();
        return bookRepository.findBooksByUserLibrary(user.getId()).stream().map(this::mapToDto).toList();
    }

    public Page<BookDto> getAllBooks(String keyword, int page, int size) {
        return bookRepository.findAllbyTitleOrAuthor(keyword, PageRequest.of(page, size)).map(this::mapToDto);
    }

    private BookDto mapToDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    public String deleteBook(Long id) {
        findById(id);
        bookRepository.deleteById(id);
        return "Book with id: " + id + " was deleted succesfully";
    }

}
