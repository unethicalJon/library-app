package com.example.library.service;

import com.example.library.dto.bookorder.Top3BooksDto;
import com.example.library.entity.Book;
import com.example.library.entity.BookOrder;
import com.example.library.entity.Order;
import com.example.library.repository.BookOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookOrderService {

    private final BookOrderRepository bookOrderRepository;

    public List<Top3BooksDto> getTopSellingBooks(int year) {
        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> results = bookOrderRepository.findTopSellingBooks(year, pageable);
        return results.stream()
                .map(result -> {
                    Book book = (Book) result[0];
                    int totalSize = ((Long) result[1]).intValue();
                    return Top3BooksDto.convertToTop3BooksDto(book, totalSize, year);
                })
                .collect(Collectors.toList());
    }

    public List<BookOrder> getAcceptedBookOrders() {
        return bookOrderRepository.findAcceptedByIdAsc();
    }

    public List<BookOrder> getAllBookOrders() {
        return bookOrderRepository.findAllByIdAsc();
    }
}
