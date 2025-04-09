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
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
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
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // BookOrders Sheet
        Sheet bookOrderSheet = workbook.createSheet("Book Orders");

        // Add title for Book Orders
        Row bookOrderTitleRow = bookOrderSheet.createRow(2);
        Cell bookOrderTitleCell = bookOrderTitleRow.createCell(3); // Start at column D (index 3)
        bookOrderTitleCell.setCellValue("All Orders and their related BookOrders");
        Font titleFont = bookOrderSheet.getWorkbook().createFont();
        titleFont.setBold(true);  // Apply bold
        titleFont.setFontHeightInPoints((short) 15);  // Set font size to 15
        CellStyle titleStyle = bookOrderSheet.getWorkbook().createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        bookOrderTitleCell.setCellStyle(titleStyle);
        bookOrderSheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 6));

        // Create header row for BookOrders at row 5, column C
        Row bookOrderHeaderRow = bookOrderSheet.createRow(4);
        String[] bookOrderHeaders = {"Order ID", "Serial Number", "User", "Book Title", "Size", "Value"};
        for (int i = 0; i < bookOrderHeaders.length; i++) {
            Cell cell = bookOrderHeaderRow.createCell(i + 2); // Start at column C
            cell.setCellValue(bookOrderHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        // Styles setup
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        CellStyle greenSubtotalStyle = workbook.createCellStyle();
        greenSubtotalStyle.cloneStyleFrom(dataStyle);
        greenSubtotalStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        greenSubtotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle blueTotalStyle = workbook.createCellStyle();
        blueTotalStyle.cloneStyleFrom(dataStyle);
        blueTotalStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        blueTotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Data writing
        int bookOrderRowNum = 5;
        int orderNumber = 1;
        long currentOrderId = -1;
        int orderStartRow = -1;

        double sizeSubtotal = 0;
        double valueSubtotal = 0;
        double grandTotalSize = 0;
        double grandTotalValue = 0;

        for (BookOrder bookOrder : bookOrders) {
            long orderId = bookOrder.getOrder().getId();
            String userName = bookOrder.getOrder().getUser().getName() + " " + bookOrder.getOrder().getUser().getSurname();

            // New order block
            if (currentOrderId != -1 && currentOrderId != orderId) {
                Row subtotalRow = bookOrderSheet.createRow(bookOrderRowNum++);
                subtotalRow.createCell(2).setCellValue("");
                subtotalRow.createCell(3).setCellValue("");
                subtotalRow.createCell(4).setCellValue("");
                subtotalRow.createCell(5).setCellValue("");
                subtotalRow.createCell(6).setCellValue(sizeSubtotal);
                subtotalRow.createCell(7).setCellValue(valueSubtotal);
                for (int j = 2; j <= 7; j++) {
                    Cell cell = subtotalRow.getCell(j);
                    if (cell != null) cell.setCellStyle(greenSubtotalStyle);
                }

                if (orderStartRow < bookOrderRowNum - 2) {
                    bookOrderSheet.addMergedRegion(new CellRangeAddress(orderStartRow, bookOrderRowNum - 2, 2, 2));
                }

                sizeSubtotal = 0;
                valueSubtotal = 0;
            }

            if (currentOrderId != orderId) {
                orderStartRow = bookOrderRowNum;
            }

            currentOrderId = orderId;

            // Data row
            Row row = bookOrderSheet.createRow(bookOrderRowNum++);
            row.createCell(2).setCellValue(orderId);
            row.createCell(3).setCellValue(orderNumber++);
            row.createCell(4).setCellValue(userName);
            row.createCell(5).setCellValue(bookOrder.getBook().getTitle());
            row.createCell(6).setCellValue(bookOrder.getSize());
            row.createCell(7).setCellValue(bookOrder.getValue());

            for (int j = 0; j < bookOrderHeaders.length; j++) {
                row.getCell(j + 2).setCellStyle(dataStyle);
            }

            // Subtotals
            sizeSubtotal += bookOrder.getSize();
            valueSubtotal += bookOrder.getValue();
            grandTotalSize += bookOrder.getSize();
            grandTotalValue += bookOrder.getValue();
        }

        // Grand total
        Row totalRow = bookOrderSheet.createRow(bookOrderRowNum++);
        totalRow.createCell(2).setCellValue("Grand Total");
        totalRow.createCell(3).setCellValue("");
        totalRow.createCell(4).setCellValue("");
        totalRow.createCell(5).setCellValue("");
        totalRow.createCell(6).setCellValue(grandTotalSize);
        totalRow.createCell(7).setCellValue(grandTotalValue);
        for (int j = 2; j <= 7; j++) {
            Cell cell = totalRow.getCell(j);
            if (cell != null) cell.setCellStyle(blueTotalStyle);
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
