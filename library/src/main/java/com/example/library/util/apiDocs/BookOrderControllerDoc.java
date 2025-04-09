package com.example.library.util.apiDocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class BookOrderControllerDoc {

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get top-selling books",
            description = "Retrieves a list of the top 3 best-selling books for a given year.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Top-selling books retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GetTopSellingBooksDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Export bookorders to an Excel file",
            description = "Allows an admin to export an Excel file of BookOrders.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File exported successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface ExportBookOrdersDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Generates the invoice of an order in a pdf format",
            description = "Allows an admin to generate a PDF File that contains information about an order's number, buyer, " +
                    "seller, books sold, library where purchased and total price including vat.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File exported successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GenerateInvoicePdf {}
}
