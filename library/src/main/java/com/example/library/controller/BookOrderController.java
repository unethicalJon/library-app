package com.example.library.controller;

import com.example.library.dto.bookorder.SimpleBookOrderDto;
import com.example.library.dto.bookorder.Top3BooksDto;
import com.example.library.service.BookOrderService;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.BookOrderController.BASE)
public class BookOrderController {

    private final BookOrderService bookOrderService;

    @GetMapping("/top-3")
    public ResponseEntity<List<Top3BooksDto>> getTopSellingBooks(@RequestParam int year) {
        List<Top3BooksDto> topBooks = bookOrderService.getTopSellingBooks(year);
        return ResponseEntity.ok(topBooks);
    }
}
