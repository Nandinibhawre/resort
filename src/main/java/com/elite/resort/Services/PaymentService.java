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
    private final BookingRepo bookingRepository;
    private final EmailService emailService;

    public Payment makePayment(String bookingId, String method, String transactionId) {

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

        paymentRepository.save(payment);

        // 4️⃣ Update booking status → CONFIRMED
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);

        // 5️⃣ Send confirmation email
        emailService.sendPaymentSuccessEmail(
                booking.getUserId(),
                booking.getRoomId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getTotalAmount()
        );

        return payment;
    }

}