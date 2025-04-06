package com.example.library.export;


import com.example.library.entity.BookOrder;
import com.example.library.entity.Order;
import com.example.library.service.OrderService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class Invoice {

    private final OrderService orderService;

    public byte[] generateInvoicePdf(Long orderId) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        Order order = orderService.findById(orderId);

        // Helper paragraph for space
        Paragraph onesp = createParagraphWithFontSize("\n", 8);

        // Logo
        InputStream logoStream = getClass().getClassLoader().getResourceAsStream("static/images/EDEN BOOKS.png");
        if (logoStream == null) {
            throw new FileNotFoundException("Logo image not found");
        }
        ImageData imageData = ImageDataFactory.create(logoStream.readAllBytes());
        Image logo = new Image(imageData);
        logo.scaleToFit(100, 100);
        logo.setFixedPosition(30, 700);

        document.add(logo);

        // Title
        Paragraph title = createParagraphWithFontSize("Order Number: " + order.getOrderNumber(), 14)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(title);

        // Order Reference
        String UserDetail = order.getUser().getName() + " " + order.getUser().getSurname();
        document.add(createParagraphWithFontSize("Order year: " + order.getYear() + "\n"
                + "Date of invoice: " + LocalDate.now() + "\n"
                + "Created by: " + UserDetail, 8)
                .setTextAlignment(TextAlignment.RIGHT));

        // Space
        document.add(onesp);
        document.add(onesp);

        // From and To Tables
        float[] columnWidths = {270f, 270f};
        Table labelTable = new Table(columnWidths);
        labelTable.setWidth(UnitValue.createPercentValue(100));

        Cell fromLabel = new Cell(1, 1)
                .add(createParagraphWithDefaultFontSize("From"))
                .setBorder(null);

        Cell toLabel = new Cell(1, 1)
                .add(createParagraphWithDefaultFontSize("To"))
                .setBorder(null);

        labelTable.addCell(fromLabel);
        labelTable.addCell(toLabel);

        document.add(labelTable);

        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        Cell fromCell = new Cell()
                .add(createParagraphWithDefaultFontSize("Eden Books Shpk").setBold())
                .add(createParagraphWithDefaultFontSize("Rr. Petro Nini Luarasi\n1001 Tirana\n\n"))
                .add(createParagraphWithDefaultFontSize("Phone: 069 751 6770"))
                .add(createParagraphWithDefaultFontSize("Email: edenbooks@gmail.om"))
                .add(createParagraphWithDefaultFontSize("Web: www.edenbooks.al\n\n\n"))
                .setBackgroundColor(new DeviceRgb(211, 211, 211));

        Cell toCell = new Cell()
                .add(createParagraphWithDefaultFontSize("KLIENT").setBold())
                .add(createParagraphWithDefaultFontSize(UserDetail))
                .add(createParagraphWithDefaultFontSize("\n\n" + order.getUser().getEmail()));

        table.addCell(fromCell);
        table.addCell(toCell);

        document.add(table);

        // Space
        document.add(onesp);

        // Library
        Table libraryTable = new Table(columnWidths);
        libraryTable.setWidth(UnitValue.createPercentValue(100));
        libraryTable.setTextAlignment(TextAlignment.LEFT);

        Cell singleCell = new Cell().add(createParagraphWithDefaultFontSize(order.getUser().getLibrary().getName()));
        libraryTable.addCell(singleCell);
        document.add(libraryTable);

        // Alb Currency
        Paragraph currencyParagraph = createParagraphWithDefaultFontSize("Amount in Albanian Lekë currency");
        currencyParagraph.setTextAlignment(TextAlignment.RIGHT);
        document.add(currencyParagraph);

        // Table for items
        Table itemTable = new Table(new float[]{4, 2, 2, 2, 2});
        itemTable.setWidthPercent(100);

        itemTable.addHeaderCell(createParagraphWithDefaultFontSize("Description"));
        itemTable.addHeaderCell(createParagraphWithDefaultFontSize("Vat"));
        itemTable.addHeaderCell(createParagraphWithDefaultFontSize("Item Price"));
        itemTable.addHeaderCell(createParagraphWithDefaultFontSize("Qty"));
        itemTable.addHeaderCell(createParagraphWithDefaultFontSize("Total (Excl. Vat)"));

        double sumOfTotals= 0.0;
        double sumOfTotalsWithVat = 0.0;

        for (BookOrder bookOrder: order.getBookOrders()) {
            itemTable.addCell(createParagraphWithDefaultFontSize(bookOrder.getBook().getTitle()));
            itemTable.addCell(createParagraphWithDefaultFontSize("20%"));
            Double price = bookOrder.getBook().getPrice();
            itemTable.addCell(createParagraphWithDefaultFontSize(String.valueOf(price)));
            int size = bookOrder.getSize();
            itemTable.addCell(createParagraphWithDefaultFontSize(String.valueOf(size)));
            Double total = price * size;
            Double totalWithVat = total + total * 0.2;
            itemTable.addCell(createParagraphWithDefaultFontSize(String.valueOf(total)));

            sumOfTotals += total;
            sumOfTotalsWithVat += totalWithVat;

        }
            document.add(itemTable);

        // Delivery Info and Total
        Table vatTable = new Table(columnWidths);
        vatTable.setWidth(UnitValue.createPercentValue(100));

        Cell deliveryCell = new Cell()
                .add(createParagraphWithDefaultFontSize("Planned date of delivery: " + LocalDate.now().plusDays(3)))
                .add(createParagraphWithDefaultFontSize("Payment Method: Cash On Delivery"))
                .setBorder(Border.NO_BORDER);

        Table innerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth(); // Inner Table for Right Part

        Cell leftCell = new Cell()
                .add(createParagraphWithDefaultFontSize("Total (Excl. Vat) "
                        + "\n Total Vat 20%\n"
                        + "Total (Incl. Vat)"))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(Border.NO_BORDER);

        Cell rightCell = new Cell()
                .add(createParagraphWithDefaultFontSize(sumOfTotals + "\n" + sumOfTotals * 0.2 + "\n" + sumOfTotalsWithVat))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER);

        innerTable.addCell(leftCell); // Add cells to innner table
        innerTable.addCell(rightCell);

        Cell totalCell = new Cell(1, 2) // Add inner table to main cell
                .add(innerTable)
                .setBorder(Border.NO_BORDER);

        vatTable.addCell(deliveryCell); // Add cells to main table
        vatTable.addCell(totalCell);
        document.add(vatTable);

        // Space
        document.add(onesp);

        // Emer, mbiemer, firme
        Table nameTable = new Table(2);
        nameTable.setWidth(UnitValue.createPercentValue(100));

        nameTable.addCell(new Cell()
                .add(createParagraphWithDefaultFontSize("Emer, Mbiemer, Firmë"))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(Border.NO_BORDER));

        nameTable.addCell(new Cell()
                .add(createParagraphWithDefaultFontSize("Emer, Mbiemer, Firmë"))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER));

        document.add(nameTable);

        // Space
        document.add(onesp);

        // Straight Lines
        Table lineTable = new Table(UnitValue.createPercentArray(new float[]{20, 60, 20}));
        lineTable.setWidth(UnitValue.createPercentValue(100));

        Cell leftLineCell = new Cell()
                .add(new Paragraph(" ").setBorderBottom(new SolidBorder(0.5f)))
                .setBorder(Border.NO_BORDER);

        Cell middleCell = new Cell().setBorder(Border.NO_BORDER); // Middle Cell empty to create illusion of space

        Cell rightLineCell = new Cell()
                .add(new Paragraph(" ").setBorderBottom(new SolidBorder(0.5f)))
                .setBorder(Border.NO_BORDER);

        lineTable.addCell(leftLineCell);
        lineTable.addCell(middleCell);
        lineTable.addCell(rightLineCell);

        document.add(lineTable);

        // Bleresi, Shitesi
        Table signatureTable = new Table(2);
        signatureTable.setWidth(UnitValue.createPercentValue(100));

        signatureTable.addCell(new Cell()
                .add(createParagraphWithDefaultFontSize("Blerësi"))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(Border.NO_BORDER));

        signatureTable.addCell(new Cell()
                .add(createParagraphWithDefaultFontSize("Shitësi"))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER));

        document.add(signatureTable);

        // Space
        document.add(onesp);

        // Footer
        float xStart = 0;
        float xEnd = 595;
        float yPosition = 50;

        PdfCanvas canvas = new PdfCanvas(pdf.getPage(pdf.getNumberOfPages()));
        canvas.moveTo(xStart, yPosition);
        canvas.lineTo(xEnd, yPosition);
        canvas.stroke();

        Paragraph footerText = createParagraphWithFontSize("Eden Books Shpk", 8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(0, yPosition - 20, PageSize.A4.getWidth());


        document.add(footerText);

        document.close();

        return outputStream.toByteArray();
    }

    private Paragraph createParagraphWithFontSize(String text, float fontSize) {
        return new Paragraph(text).setFontSize(fontSize);
    }

    private Paragraph createParagraphWithDefaultFontSize(String text) {
        return createParagraphWithFontSize(text, 9);
    }
}

