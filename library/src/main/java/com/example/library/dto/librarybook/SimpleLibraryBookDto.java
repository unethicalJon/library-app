package com.example.library.dto.librarybook;

import com.example.library.dto.book.SimpleBookDto;
import com.example.library.dto.library.SimpleLibraryDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SimpleLibraryBookDto {

    private Long id;

    @JsonProperty("Library")
    private SimpleLibraryDto simpleLibraryDto;

    @JsonProperty("Book")
    private SimpleBookDto simpleBookDto;

    private Integer stock;
}
