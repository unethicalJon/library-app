package com.example.library.export;

import com.example.library.entity.BookOrder;
import com.example.library.entity.Order;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportOrders {

    public byte[] ordersExcel(List<Order> orders, List<BookOrder> bookOrders) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        // Orders Sheet
        Sheet orderSheet = workbook.createSheet("Orders");

        // Create bold style for title
        CellStyle boldStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);
        boldStyle.setAlignment(HorizontalAlignment.LEFT);

        // Add title for Orders
        Row titleRow = orderSheet.createRow(2);
        Cell titleCell = titleRow.createCell(2);
        titleCell.setCellValue("All Time Orders Table");
        titleCell.setCellStyle(boldStyle);
        orderSheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 4));

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

        // Create header row for Orders at row 5, column C
        int startRow = 4;
        int startCol = 2;
        Row headerRow = orderSheet.createRow(startRow);
        String[] headers = {"ID", "Order Number", "Status", "User", "Admin Note", "User Note"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(startCol + i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows for Orders
        int rowNum = startRow + 1;
        for (Order order : orders) {
            Row row = orderSheet.createRow(rowNum++);
            row.createCell(startCol).setCellValue(order.getId());
            row.createCell(startCol + 1).setCellValue(order.getOrderNumber());
            row.createCell(startCol + 2).setCellValue(order.getStatus().name());
            row.createCell(startCol + 3).setCellValue(order.getUser().getUsername());
            row.createCell(startCol + 4).setCellValue(order.getAdminNote());
            row.createCell(startCol + 5).setCellValue(order.getUserNote());

            // Apply style to all cells in the row
            for (int i = 0; i < headers.length; i++) {
                row.getCell(startCol + i).setCellStyle(dataStyle);
            }
        }

        // Set column width for Orders
        for (int i = 0; i < headers.length; i++) {
            orderSheet.setColumnWidth(startCol + i, 8000);
        }

        // BookOrders Sheet
        Sheet bookOrderSheet = workbook.createSheet("Book Orders");

        // Add title for Book Orders
        Row bookOrderTitleRow = bookOrderSheet.createRow(2);
        Cell bookOrderTitleCell = bookOrderTitleRow.createCell(2);
        bookOrderTitleCell.setCellValue("All Related BookOrders");
        bookOrderTitleCell.setCellStyle(boldStyle);
        bookOrderSheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 4));

        // Create header row for BookOrders at row 5, column C
        Row bookOrderHeaderRow = bookOrderSheet.createRow(4);
        String[] bookOrderHeaders = {"ID", "Book Title", "Order ID", "Size", "Value"};
        for (int i = 0; i < bookOrderHeaders.length; i++) {
            Cell cell = bookOrderHeaderRow.createCell(i + 2); // Start at column C
            cell.setCellValue(bookOrderHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows for BookOrders
        int bookOrderRowNum = 5; // Start at row 6
        for (BookOrder bookOrder : bookOrders) {
            Row row = bookOrderSheet.createRow(bookOrderRowNum++);
            row.createCell(2).setCellValue(bookOrder.getId());
            row.createCell(3).setCellValue(bookOrder.getBook().getTitle());
            row.createCell(4).setCellValue(bookOrder.getOrder().getId());
            row.createCell(5).setCellValue(bookOrder.getSize());
            row.createCell(6).setCellValue(bookOrder.getValue());

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
