package com.example.library.service;

import com.example.library.datatype.Genre;
import com.example.library.datatype.Section;
import com.example.library.dto.book.BookDto;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public void save(Book book) {
        bookRepository.save(book);
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


    public List<Book> getBooksFromUserLibrary() {
        User user = userService.loggedInUser();
        return bookRepository.findBooksOfUser(user.getId()).stream().toList();
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
        return "Book with id: " + id + " was deleted successfully";
    }

}
