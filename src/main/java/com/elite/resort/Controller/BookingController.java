package com.elite.resort.Controller;

import com.elite.resort.DTO.BookingRequest;
import com.elite.resort.Model.Booking;
import com.elite.resort.Security.JwtUtil;
import com.elite.resort.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final JwtUtil jwtUtil;

    // ================= CREATE BOOKING =================
    @PostMapping
    public Booking bookRoom(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody BookingRequest request) {

        // ✅ REMOVE "Bearer "
        String token = authHeader.substring(7);

        // ✅ EXTRACT USER ID FROM TOKEN
        String userId = jwtUtil.extractId(token);

        return bookingService.createBooking(userId, request);
    }

    // ================= CANCEL BOOKING =================
    @PutMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable String id) {

        bookingService.cancelBooking(id);
        return "Booking cancelled successfully";
    }
}
