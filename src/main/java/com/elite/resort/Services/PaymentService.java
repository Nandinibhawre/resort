package com.elite.resort.Services;

import com.elite.resort.DTO.PaymentRequest;
import com.elite.resort.Exceptions.BadRequestException;
import com.elite.resort.Exceptions.ResourceNotFoundException;
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
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepository;
    private final UserRepo userRepository;
    private final BookingRepo bookingRepository;
    private final EmailService emailService;


    public Payment makePayment(String bookingId, String method, String transactionId)
    {

        // 1️⃣ Find booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // 2️⃣ Check booking status
        if (!"PENDING_PAYMENT".equals(booking.getStatus())) {
            throw new BadRequestException("Payment already completed or booking invalid");
        }

        // 3️⃣ Create payment
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setMethod(method);
        payment.setTransactionId(transactionId);
        payment.setAmount(booking.getTotalAmount());
        payment.setStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());

        // ✅ SAVE PAYMENT FIRST (important)
        Payment savedPayment = paymentRepository.save(payment);

        // ✅ Update booking AFTER payment saved
        booking.setStatus("CONFIRMED");
        booking.setPaymentId(savedPayment.getId());

        // ⭐ set createdAt if not already set
        if (booking.getPaymentDoneAt() == null) {
            booking.setPaymentDoneAt(LocalDateTime.now());
        }

        bookingRepository.save(booking);


        // 4️⃣ Update booking status → CONFIRMED
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);

        User user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        emailService.sendPaymentSuccessEmail(
                user.getEmail(),          // ✅ REAL EMAIL (not userId)
                booking.getCheckIn(),
                booking.getCheckOut(),
                payment.getAmount()
        );



        // ❌ Update payment
        payment.setStatus("CONFIRMED");
        paymentRepository.save(payment);

        // ❌ Update booking
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);


        // 📧 Send cancellation email
        emailService.sendBookingCancellationEmail(
                user.getEmail(),
                booking.getRoomId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getTotalAmount()
        );
        return payment;
    }
    // ================= CANCEL PAYMENT + BOOKING =================
    public void cancelPaymentAndBooking(String paymentId) {

        // 🔍 Find payment
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if ("CANCELLED".equals(payment.getStatus())) {
            throw new RuntimeException("Payment already cancelled");
        }

        // 🔍 Find booking
        Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // ❌ Update payment
        payment.setStatus("CANCELLED");
        paymentRepository.save(payment);

        // ❌ Update booking
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // 🔍 Get user email
        User user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 📧 Send cancellation email
        emailService.sendBookingCancellationEmail(
                user.getEmail(),
                booking.getRoomId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getTotalAmount()
        );
    }
}