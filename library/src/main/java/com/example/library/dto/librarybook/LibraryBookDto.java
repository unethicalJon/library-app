package com.example.library.dto.librarybook;

import lombok.Data;

@Data
public class LibraryBookDto {
    private Long libraryId;
    private Long bookId;
    private int stock;
}
