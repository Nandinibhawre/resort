package com.elite.resort.Services;

import com.elite.resort.Model.Booking;
import com.elite.resort.Model.Invoice;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(Booking booking, Invoice invoice) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("HOTEL BOOKING INVOICE"));
        document.add(new Paragraph("----------------------------"));

        document.add(new Paragraph("Booking ID: " + booking.getBookingId()));
        document.add(new Paragraph("Check-in Date: " + booking.getCheckIn()));
        document.add(new Paragraph("Check-out Date: " + booking.getCheckOut()));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Room Amount: ₹" + invoice.getRoomAmount()));
        document.add(new Paragraph("Final Amount: ₹" + invoice.getFinalAmount()));
        document.add(new Paragraph("Payment Status: " + invoice.getPaymentStatus()));

        document.close();

        return out.toByteArray();
    }
}
