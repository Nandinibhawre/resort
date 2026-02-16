package com.elite.resort.Services;

import com.elite.resort.DTO.BookingRequest;
import com.elite.resort.Exceptions.BadRequestException;
import com.elite.resort.Exceptions.ResourceNotFoundException;
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

        // ❌ Room ID missing
        if (request.getRoomId() == null) {
            throw new BadRequestException("Room ID is required");
        }

        // ❌ Room not found
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // ❌ Room unavailable
        if (!room.isAvailable()) {
            throw new BadRequestException("Room is not available for booking");
        }

        // ❌ User not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // ❌ Invalid dates
        long days = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        if (days <= 0) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        double total = days * room.getPricePerNight();

        // ✅ Create booking
        Booking booking = new Booking();
        booking.setRoomId(room.getRoomId());
        booking.setUserId(user.getUserId());
        booking.setCheckIn(request.getCheckInDate());
        booking.setCheckOut(request.getCheckOutDate());
        booking.setTotalAmount(total);
        booking.setStatus("CONFIRMED");

        bookingRepository.save(booking);

        // ✅ Mark room unavailable
        room.setAvailable(false);
        roomRepository.save(room);

        // ✅ Send confirmation email
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
