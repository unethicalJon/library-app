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


public class UserControllerDoc {

        @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
        @Retention(RetentionPolicy.RUNTIME)
        @Operation(
                summary = "Register a new user",
                description = "Allows an admin to create a new user in the system.\n\n" +
                        "**Validations:**\n" +
                        "- Name, surname, username, password, and email are not null or empty.\n" +
                        "- Username should be a unique value.\n" +
                        "- Password must contain at least 8 characters and at least 1 special character.",
                responses = {
                        @ApiResponse(responseCode = "201", description = "User created successfully"),
                        @ApiResponse(responseCode = "400", description = "Bad request", content = {
                                @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ResponseError.class))
                        }),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
                }
        )
        public @interface RegisterUserDoc {}

        @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
        @Retention(RetentionPolicy.RUNTIME)
        @Operation(
                summary = "Get user profile",
                description = "Allows an admin to retrieve a user's details by ID.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found", content = {
                                @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseError.class))
                        }),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
                }
        )
        public @interface GetUserProfileDoc {}

        @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
        @Retention(RetentionPolicy.RUNTIME)
        @Operation(
                summary = "Update user profile",
                description = "Allows a user to update his own profile details.",
                responses = {
                        @ApiResponse(responseCode = "200", description = "User updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data", content = {
                                @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseError.class))
                        }),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
                }
        )
        public @interface UpdateUserProfileDoc {}

        @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
        @Retention(RetentionPolicy.RUNTIME)
        @Operation(
                summary = "Change user password",
                description = "Allows an admin to change a user's password.\n\n" +
                        "**Validations:**\n" +
                        "- Password must contain at least 8 characters and at least 1 special character. \n" +
                        "- New password and Confirmation password should match",
                responses = {
                        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid password format", content = {
                                @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseError.class))
                        }),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
                }
        )
        public @interface UpdateUserPasswordDoc {}

        @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
        @Retention(RetentionPolicy.RUNTIME)
        @Operation(
                summary = "Activate a user",
                description = "Allows an admin to activate a deactivated user." +
                        "  **Upon activating the user an activation confirmation email will be sent" +
                        "to their email address**",
                responses = {
                        @ApiResponse(responseCode = "200", description = "User activated successfully"),
                        @ApiResponse(responseCode = "403", description = "Access denied"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
                }
        )
        public @interface ActivateUserDoc {}
    }
