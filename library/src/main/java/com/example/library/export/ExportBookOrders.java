package com.example.library.export;

import com.example.library.entity.BookOrder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportBookOrders {

    public byte[] bookOrdersExcel(List<BookOrder> bookOrders) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        // Create bold style for title
        CellStyle boldStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);
        boldStyle.setAlignment(HorizontalAlignment.LEFT);

        // Create header style for both sheets
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Create data style
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setAlignment(HorizontalAlignment.LEFT);

        // BookOrders Sheet
        Sheet bookOrderSheet = workbook.createSheet("Book Orders");

        // Add title for Book Orders
        Row bookOrderTitleRow = bookOrderSheet.createRow(2);
        Cell bookOrderTitleCell = bookOrderTitleRow.createCell(2);
        bookOrderTitleCell.setCellValue("All Orders and their related BookOrders");
        bookOrderTitleCell.setCellStyle(boldStyle);
        bookOrderSheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 4));

        // Create header row for BookOrders at row 5, column C
        Row bookOrderHeaderRow = bookOrderSheet.createRow(4);
        String[] bookOrderHeaders = {"BookOrder Number", "User", "User Note", "Admin Note", "Order Status", "Book Title", "Size", "Value"};
        for (int i = 0; i < bookOrderHeaders.length; i++) {
            Cell cell = bookOrderHeaderRow.createCell(i + 2); // Start at column C
            cell.setCellValue(bookOrderHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows for BookOrders
        int bookOrderRowNum = 5; // Start at row 6
        int orderNumber = 1; // Start orders at 1
        for (BookOrder bookOrder : bookOrders) {
            Row row = bookOrderSheet.createRow(bookOrderRowNum++);
            row.createCell(2).setCellValue(orderNumber++);
            row.createCell(3).setCellValue(bookOrder.getOrder().getUser().getName() + " " + bookOrder.getOrder().getUser().getSurname());
            row.createCell(4).setCellValue(bookOrder.getOrder().getUserNote());
            row.createCell(5).setCellValue(bookOrder.getOrder().getAdminNote());
            row.createCell(6).setCellValue(bookOrder.getOrder().getStatus().name);
            row.createCell(7).setCellValue(bookOrder.getBook().getTitle());
            row.createCell(8).setCellValue(bookOrder.getSize());
            row.createCell(9).setCellValue(bookOrder.getValue());

            // Apply style to all cells in the row
            for (int i = 0; i < bookOrderHeaders.length; i++) {
                row.getCell(i + 2).setCellStyle(dataStyle);
            }
        }

        // Set column width for Book Orders sheet
        for (int i = 0; i < bookOrderHeaders.length; i++) {
            bookOrderSheet.setColumnWidth(i + 2, 8000);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
