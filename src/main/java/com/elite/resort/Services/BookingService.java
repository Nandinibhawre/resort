package com.elite.resort.Services;

import com.elite.resort.DTO.BookingRequest;
import com.elite.resort.Exceptions.BadRequestException;
import com.elite.resort.Exceptions.ResourceNotFoundException;
import com.elite.resort.Exceptions.RoomUnavailableException;
import com.elite.resort.Model.Booking;
import com.elite.resort.Model.Room;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.BookingRepo;
import com.elite.resort.Repository.RoomRepo;
import com.elite.resort.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepo bookingRepository;
    private final RoomRepo roomRepository;
    private final UserRepo userRepository;
    private final EmailService emailService;
    public Booking createBooking(String userId, String roomId, BookingRequest request) {

        // ✅ Find room by path id
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // ✅ Find user from token id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ 1. Check room active status
        if (!room.isAvailable()) {
            throw new RoomUnavailableException("Room is currently unavailable for booking");
        }

        // ✅ Check overlapping bookings
        List<Booking> conflicts =
                bookingRepository.findConflictingBookings(
                        roomId,
                        request.getCheckInDate(),
                        request.getCheckOutDate()
                );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room already booked for selected dates");
        }

        // ✅ Calculate days
        long days = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        if (days <= 0) {
            throw new RuntimeException("Check-out must be after check-in");
        }

        double total = days * room.getPricePerNight();

        // ✅ Create booking
        Booking booking = new Booking();
        booking.setRoomId(room.getRoomId());
        booking.setUserId(user.getUserId());
        booking.setCheckIn(request.getCheckInDate());
        booking.setCheckOut(request.getCheckOutDate());
        booking.setTotalAmount(total);
        booking.setStatus("PENDING_PAYMENT");

        bookingRepository.save(booking);

        // ❌ DO NOT mark room unavailable anymore
        // because availability is now date-based

        // ✅ Send email
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

        // ❌ Booking not found
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // ✅ Make room available again
        Room room = roomRepository.findById(booking.getRoomId())
                .orElse(null);

        if (room != null) {
            room.setAvailable(true);
            roomRepository.save(room);
        }
    }
}
