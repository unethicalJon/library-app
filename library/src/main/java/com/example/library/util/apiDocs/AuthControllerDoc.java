package com.example.library.util.apiDocs;

import com.example.library.dto.auth.AuthenticationResponseDto;
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

public class AuthControllerDoc {

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Log in a user",
            description = "Authenticates the user and provides a JWT token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT token generated successfully", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthenticationResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface AuthenticationDoc {}
}

