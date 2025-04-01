package com.example.library.util.apiDocs;

import com.example.library.dto.general.ResponseError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class LibraryBookControllerDoc {

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Update stock of library books",
            description = "Allows an admin to update the stock of books in a specific library.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Library not found", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface UpdateStockDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get all library books",
            description = "Retrieves a paginated list of all books available in the library system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GetLibraryBooksDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get available books",
            description = "Retrieves a paginated list of books that are currently available in a specific library. \n\n" +
                    "If no Library ID is provided, it retrieves available books across all libraries.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Available books retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GetAvailableBooksDoc {}
}

