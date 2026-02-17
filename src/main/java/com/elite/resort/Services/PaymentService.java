package com.elite.resort.Services;

import com.elite.resort.DTO.PaymentRequest;
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

    private final PaymentRepo paymentRepo;
    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;
    private final EmailService emailService;

    public Payment makePayment(PaymentRequest request) {

        // 1️⃣ Get booking
        Booking booking = bookingRepo.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 2️⃣ Booking must be
        if (!"PENDING".equals(booking.getStatus())) {
            throw new RuntimeException("Payment already completed or booking cancelled");
        }

        // 3️⃣ Create payment
        Payment payment = new Payment();
        payment.setBookingId(booking.getBookingId());
        payment.setAmount(booking.getTotalAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus("SUCCESS"); // assume success for now
        payment.setTransactionId(request.getTransactionId());
        payment.setPaidAt(LocalDateTime.now());

        Payment savedPayment = paymentRepo.save(payment);

        // 4️⃣ Update booking → CONFIRMED
        booking.setStatus("CONFIRMED");
        bookingRepo.save(booking);

        // 5️⃣ Send confirmation email AFTER payment success
        User user = userRepo.findById(booking.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepo.findById(booking.getRoomId())
                .orElseThrow(() -> new RuntimeException("User not found"));

                return savedPayment;
    }
}