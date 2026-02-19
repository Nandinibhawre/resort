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
    private final RoomRepo roomRepository;

//    public Payment makePayment(String bookingId, String method, String transactionId)
//    {
//
//        // 1️⃣ Find booking
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
//
//        // 2️⃣ Check booking status
//        if (!"PENDING_PAYMENT".equals(booking.getStatus())) {
//            throw new BadRequestException("Payment already completed or booking invalid");
//        }
//
//        // 3️⃣ Create payment
//        Payment payment = new Payment();
//        payment.setBookingId(bookingId);
//        payment.setMethod(method);
//        payment.setTransactionId(transactionId);
//        payment.setAmount(booking.getTotalAmount());
//        payment.setStatus("SUCCESS");
//        payment.setPaidAt(LocalDateTime.now());
//
//        // ✅ SAVE PAYMENT FIRST (important)
//        Payment savedPayment = paymentRepository.save(payment);
//
//        // ✅ Update booking AFTER payment saved
//        booking.setStatus("CONFIRMED");
//        booking.setPaymentId(savedPayment.getId());
//
//        // ⭐ set createdAt if not already set
//        if (booking.getPaymentDoneAt() == null) {
//            booking.setPaymentDoneAt(LocalDateTime.now());
//        }
//
//        bookingRepository.save(booking);
//
//
//        // 4️⃣ Update booking status → CONFIRMED
//        booking.setStatus("CONFIRMED");
//        bookingRepository.save(booking);
//
//        User user = userRepository.findById(booking.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        emailService.sendPaymentSuccessEmail(
//                user.getEmail(),          // ✅ REAL EMAIL (not userId)
//                booking.getCheckIn(),
//                booking.getCheckOut(),
//                payment.getAmount()
//        );
//
//
//
//        // ❌ Update payment
//        payment.setStatus("CONFIRMED");
//        paymentRepository.save(payment);
//
//        // ❌ Update booking
//        booking.setStatus("CONFIRMED");
//        bookingRepository.save(booking);
//
//
//        // 📧 Send cancellation email
//        emailService.sendBookingCancellationEmail(
//                user.getEmail(),
//                booking.getRoomId(),
//                booking.getCheckIn(),
//                booking.getCheckOut(),
//                booking.getTotalAmount()
//        );
//        return payment;
//    }

    // ================= CREATE BOOKING =================
    public Booking createBooking(String userId, String roomId, BookingRequest request) {

        // 1️⃣ Find room
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // 2️⃣ Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 3️⃣ Check room active
        if (!room.isAvailable()) {
            throw new RoomUnavailableException("Room is currently unavailable");
        }

        // 4️⃣ Check date conflicts
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                roomId,
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        if (!conflicts.isEmpty()) {
            throw new RoomUnavailableException("Room already booked for selected dates");
        }

        // 5️⃣ Validate dates
        long days = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        if (days <= 0) {
            throw new RoomUnavailableException("Check-out must be after check-in");
        }

        // 6️⃣ Calculate total
        double total = days * room.getPricePerNight();

        // 7️⃣ Create booking
        Booking booking = new Booking();
        booking.setRoomId(room.getRoomId());
        booking.setUserId(user.getUserId());
        booking.setCheckIn(request.getCheckInDate());
        booking.setCheckOut(request.getCheckOutDate());
        booking.setTotalAmount(total);
        booking.setStatus("PENDING_PAYMENT");

        // 🔥 createdAt & updatedAt AUTO handled by Mongo Auditing
        bookingRepository.save(booking);

        // 8️⃣ Send confirmation email
        emailService.sendBookingConfirmationEmail(
                user.getEmail(),
                user.getName(),
                room.getRoomNumber(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        return booking;
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