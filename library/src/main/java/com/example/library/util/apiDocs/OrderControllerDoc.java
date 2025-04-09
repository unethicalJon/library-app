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

public class OrderControllerDoc {

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Create a book order",
            description = "Allows a user to create a book order. \n\n" +
                    "Checks if there is sufficient stock in the library to fulfill the client's request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface CreateOrderDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Send order for approval",
            description = "Allows a user to submit an order for approval. \n\n" +
                    "**Once an order is sent for approval it's status is moved from CREATAED to PENDING " +
                    "and the order can no longer be updated.**",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order sent for approval successfully"),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface SendOrderForApprovalDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Approve an order",
            description = "Allows an admin to accept or reject a pending order. \n\n" +
                    "In case of rejection admin can provide a note describing the reason of rejection. " +
                    "When an order is accepted the stock of ordered books is deducted automatically.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order approved successfully"),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface ApproveOrderDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Update an order",
            description = "Allows a user to update an existing order. \n\n" +
                    "**Order can only be updated if it's in status created.** \n\n" +
                    "User can: \n" +
                    "- Add new books to the order. \n" +
                    "- Update existing books in the order. (their stock) \n" +
                    "- Remove books from order. **(if they are left out from the list)** \n\n" +
                    "**Available stock is validated to meet user's requirements.**",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseError.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface UpdateOrderDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get all orders",
            description = "Retrieves a paginated list of all orders.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GetOrdersDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get pending orders",
            description = "Retrieves a paginated list of pending orders for admins.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pending orders retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface GetPendingOrdersDoc {}

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Export orders to an excel file",
            description = "Allows an admin to export an excel file of orders. \n\n" +
                    "**orders.xlsx contains a workbook with two sheets:** \n" +
                    "-Orders \n" +
                    "-BookOrders related to the orders",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File exported successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public @interface ExportOrdersDoc {}
}


