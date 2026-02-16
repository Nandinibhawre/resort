package com.elite.resort.Services;

import com.elite.resort.DTO.BookingRequest;
import com.elite.resort.Model.Booking;
import com.elite.resort.Model.Room;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.BookingRepo;
import com.elite.resort.Repository.RoomRepo;
import com.elite.resort.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepo bookingRepository;
    private final RoomRepo roomRepository;
    private final UserRepo userRepository;
    private final EmailService emailService;

    public Booking createBooking(String userId, BookingRequest request) {

        if (request.getRoomId() == null)
            throw new RuntimeException("RoomId missing");

        // ✅ GET ROOM FROM DB
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.isAvailable())
            throw new RuntimeException("Room not available");

        // ✅ GET USER FROM DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ CALCULATE DAYS
        long days = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate());

        if (days <= 0)
            throw new RuntimeException("Invalid booking dates");

        double total = days * room.getPricePerNight();

        // ✅ CREATE BOOKING
        Booking booking = new Booking();
        booking.setRoomId(room.getRoomId());     // FIXED
        booking.setUserId(user.getUserId());     // FIXED
        booking.setCheckIn(request.getCheckInDate());
        booking.setCheckOut(request.getCheckOutDate());
        booking.setTotalAmount(total);
        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);

        // ✅ MARK ROOM UNAVAILABLE
        room.setAvailable(false);
        roomRepository.save(room);

        // ✅ EMAIL
        emailService.sendBookingConfirmationEmail(
                user.getEmail(),
                user.getName(),
                room.getRoomNumber(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        return booking;
    }
    public void cancelBooking(String id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // ✅ OPTIONAL: make room available again
        Room room = roomRepository.findById(booking.getRoomId())
                .orElse(null);

        if (room != null) {
            room.setAvailable(true);
            roomRepository.save(room);
        }
    }
}
