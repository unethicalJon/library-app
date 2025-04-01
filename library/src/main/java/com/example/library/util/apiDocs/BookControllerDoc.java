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

public class BookControllerDoc {

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Add a new book",
            description = "Allows an admin to add a new book to the library.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book created successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface AddBookDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Update book details",
            description = "Allows an admin to update details of an existing book.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Book not found", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface UpdateBookDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Retrieve user's books",
            description = "Allows a user to retrieve a list of books in their personal library.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GetUserBooksDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Retrieve all books",
            description = "Allows an admin to retrieve all books.\n\n" +
                    "Optional search with keyword for author or title and pagination support are provided.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GetAllBooksDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Delete a book",
            description = "Allows an admin to delete a book from the library.\n\n" +
                    "**Deletion will fail if the book is linked to other entities.**",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Deletion failed due to data integrity issues", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface DeleteBookDoc {}
}

