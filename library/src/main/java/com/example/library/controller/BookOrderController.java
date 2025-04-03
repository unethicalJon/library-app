package com.example.library.controller;

import com.example.library.dto.bookorder.Top3BooksDto;
import com.example.library.entity.BookOrder;
import com.example.library.export.ExportBookOrders;
import com.example.library.export.Invoice;
import com.example.library.service.BookOrderService;
import com.example.library.util.apiDocs.BookOrderControllerDoc;
import com.example.library.util.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.BookOrderController.BASE)
public class BookOrderController {

    private final BookOrderService bookOrderService;
    private final ExportBookOrders exportBookOrders;
    private final Invoice invoice;

    @GetMapping(RestConstants.BookOrderController.TOP_3)
    @BookOrderControllerDoc.GetTopSellingBooksDoc
    public ResponseEntity<List<Top3BooksDto>> getTopSellingBooks(@RequestParam int year) {
        List<Top3BooksDto> topBooks = bookOrderService.getTopSellingBooks(year);
        return ResponseEntity.ok(topBooks);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @BookOrderControllerDoc.ExportBookOrdersDoc
    @GetMapping(RestConstants.BookOrderController.EXPORT)
    public ResponseEntity<byte[]> exportBookOrders() throws IOException {

        List<BookOrder> bookOrders = bookOrderService.getAllBookOrders();

        byte[] excelData = exportBookOrders.bookOrdersExcel(bookOrders);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bookorders.xlsx")
                .body(excelData);
    }

    @PreAuthorize("hasAnyAuthority(@Role.ADMIN_NAME)")
    @GetMapping(RestConstants.BookOrderController.INVOICE)
    @BookOrderControllerDoc.GenerateInvoicePdf
    public ResponseEntity<byte[]> generateInvoice(@PathVariable(value = RestConstants.ID) Long orderId) {
        try {
            byte[] pdfBytes = invoice.generateInvoicePdf(orderId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=invoice_order_" + orderId + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


