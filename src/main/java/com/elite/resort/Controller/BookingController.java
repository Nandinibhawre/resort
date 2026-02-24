package com.elite.resort.Controller;

import com.elite.resort.DTO.BookingRequest;
import com.elite.resort.Model.Booking;
import com.elite.resort.Security.JwtUtil;
import com.elite.resort.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final JwtUtil jwtUtil;

    // ================= CREATE BOOKING =================
@PostMapping("/rooms/{roomId}")
public Booking bookRoom(
        @PathVariable String roomId,
        @RequestHeader("Authorization") String authHeader,
        @RequestBody BookingRequest request) {

    // ✅ Remove Bearer prefix
    String token = authHeader.substring(7);

    // ✅ Extract userId from JWT
    String userId = jwtUtil.extractUserId(token);

  
    return bookingService.createBooking(userId, roomId, request);
}


    // ================= CANCEL BOOKING =================
    @PutMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable String id) {

        bookingService.cancelBooking(id);
        return "Booking cancelled successfully";
    }
}
