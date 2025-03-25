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

public class LibraryControllerDoc {

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Register a new library",
            description = "Allows an admin to register a new library in the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Library registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface RegisterLibraryDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Update an existing library",
            description = "Allows an admin to update the details of an existing library.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Library updated successfully"),
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
    public @interface UpdateLibraryDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Delete a library",
            description = "Allows an admin to delete a library. \n\n" +
                    "**Deletion will fail if the library is linked to other entities.**",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Library deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Library is linked to other entities and cannot be deleted", content = {
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
    public @interface DeleteLibraryDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get a paginated list of libraries (Admin)",
            description = "Allows an admin to retrieve a list of libraries \n\n" +
                    "Optional search with keyword for name or address is provided.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Libraries retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GetLibrariesAdminDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get a library for a user",
            description = "Retrieves the library associated with the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Library retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "User's library not found", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GetLibraryUserDoc {}
}

