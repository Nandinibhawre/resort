package com.elite.resort.Services;

import com.elite.resort.Model.Booking;
import com.elite.resort.Model.Invoice;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.InvoiceRepo;
import com.elite.resort.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepo invoiceRepository;
    private final PdfService pdfService;
    private final EmailService brevoEmailService;
    private final UserRepo userRepository;
    public Invoice generateInvoiceAndSend(Booking booking, double roomPrice) {

        System.out.println("=== ENTERED generateInvoiceAndSend ===");
        System.out.println("Booking ID: " + booking.getBookingId());
        long nights = ChronoUnit.DAYS.between(
                booking.getCheckIn(),
                booking.getCheckOut()
        );

        double roomAmount = roomPrice * nights;
        double tax = roomAmount * 0.10;
        double finalAmount = roomAmount + tax;

        Invoice invoice = Invoice.builder()
                .bookingId(booking.getBookingId())
                .roomAmount(roomAmount)
                .finalAmount(finalAmount)
                .paymentStatus("PAID")
                .createdAt(LocalDateTime.now())
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Generate PDF
        byte[] pdfBytes = pdfService.generateInvoicePdf(booking, savedInvoice);

        // ✅ Fetch user email from DB
        User user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String userEmail = user.getEmail();

        // ✅ Send email using Brevo
        brevoEmailService.sendInvoiceEmail(userEmail, pdfBytes);
        System.out.println("Saving invoice to MongoDB...");



        System.out.println("✅ Invoice saved with ID: " + savedInvoice.getId());
        return savedInvoice;

    }

    public Invoice getByBookingId(String bookingId) {
        return invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

    }
}