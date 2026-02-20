package com.elite.resort.Services;

import com.elite.resort.DTO.BookingRequest;
import com.elite.resort.DTO.PaymentRequest;
import com.elite.resort.Exceptions.BadRequestException;
import com.elite.resort.Exceptions.ResourceNotFoundException;
import com.elite.resort.Exceptions.RoomUnavailableException;
import com.elite.resort.Model.Booking;
import com.elite.resort.Model.Payment;
import com.elite.resort.Model.Room;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.BookingRepo;
import com.elite.resort.Repository.PaymentRepo;
import com.elite.resort.Repository.RoomRepo;
import com.elite.resort.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepository;
    private final UserRepo userRepository;
    private final BookingRepo bookingRepository;
    private final EmailService emailService;
    private  final InvoiceService invoiceService;

    // ================= MAKE PAYMENT =================
    public Payment createPayment(String bookingId, String method, String transactionId) {

        // 1️⃣ Find booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // 2️⃣ Prevent double payment
        if (!"PENDING_PAYMENT".equals(booking.getStatus())) {
            throw new BadRequestException("Booking already paid or invalid");
        }

        // 3️⃣ Create payment
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setMethod(method);
        payment.setTransactionId(transactionId);
        payment.setAmount(booking.getTotalAmount());
        payment.setStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // 4️⃣ Confirm booking
        booking.setStatus("CONFIRMED");
        booking.setPaymentId(savedPayment.getPaymentId());
        bookingRepository.save(booking);

        invoiceService.generateInvoiceAndSend(booking, booking.getTotalAmount());
        // 5️⃣ Send success email
        User user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        emailService.sendPaymentSuccessEmail(
                user.getEmail(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                savedPayment.getAmount()
        );

        return savedPayment;
    }

    // ================= CANCEL PAYMENT + BOOKING =================
    public void cancelPaymentAndBooking(String paymentId) {

        // 1️⃣ Find payment
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if ("CANCELLED".equals(payment.getStatus())) {
            throw new BadRequestException("Payment already cancelled");
        }

        // 2️⃣ Find booking
        Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // 3️⃣ Cancel payment & booking
        payment.setStatus("CANCELLED");
        paymentRepository.save(payment);

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // 4️⃣ Send cancel email
        User user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        emailService.sendBookingCancellationEmail(
                user.getEmail(),
                booking.getRoomId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getTotalAmount()
        );
    }

}