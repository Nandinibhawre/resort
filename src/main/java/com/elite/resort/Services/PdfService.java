package com.elite.resort.Services;

import com.elite.resort.Model.Booking;
import com.elite.resort.Model.Invoice;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(Booking booking, Invoice invoice) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

// ======= TITLE =======
        Paragraph title = new Paragraph("ELITE RESORT")
                .setBold()
                .setFontSize(22)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph subtitle = new Paragraph("HOTEL BOOKING INVOICE")
                .setBold()
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);

        document.add(title);
        document.add(subtitle);
        document.add(new Paragraph("\n"));

// ======= BOOKING DETAILS TABLE =======
        float[] columnWidths = {200F, 300F};
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

// Booking ID
        table.addCell(new Cell().add(new Paragraph("Booking ID")).setBold());
        table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getBookingId()))));

// Check-in
        table.addCell(new Cell().add(new Paragraph("Check-in Date")).setBold());
        table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getCheckIn()))));

// Check-out
        table.addCell(new Cell().add(new Paragraph("Check-out Date")).setBold());
        table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getCheckOut()))));

        document.add(table);
        document.add(new Paragraph("\n"));

// ======= PAYMENT DETAILS TABLE =======
        Table paymentTable = new Table(columnWidths);
        paymentTable.setWidth(UnitValue.createPercentValue(100));

        paymentTable.addCell(new Cell().add(new Paragraph("Room Amount")).setBold());
        paymentTable.addCell(new Cell().add(new Paragraph("₹ " + invoice.getRoomAmount())));

        paymentTable.addCell(new Cell().add(new Paragraph("Final Amount")).setBold());
        paymentTable.addCell(new Cell().add(new Paragraph("₹ " + invoice.getFinalAmount()))
                .setFontSize(14)
                .setBold());

        paymentTable.addCell(new Cell().add(new Paragraph("Payment Status")).setBold());
        paymentTable.addCell(new Cell().add(new Paragraph(invoice.getPaymentStatus())));

        document.add(paymentTable);
        document.add(new Paragraph("\n\n"));

// ======= FOOTER =======
        Paragraph footer = new Paragraph("Thank you for choosing Elite Resort!")
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic();

        document.add(footer);

        document.close();
        document.close();

        return out.toByteArray();
    }
}
