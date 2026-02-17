package com.elite.resort.Controller;

import com.elite.resort.DTO.BookingRequest;
import com.elite.resort.DTO.PaymentRequest;
import com.elite.resort.Model.Booking;
import com.elite.resort.Model.Payment;
import com.elite.resort.Security.JwtUtil;
import com.elite.resort.Services.BookingService;
import com.elite.resort.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
@   Autowired
    private BookingService bookingService;
    @Autowired
private JwtUtil jwtUtil;
    @PostMapping("/rooms/{roomId}")
    public Booking bookRoom(
            @PathVariable String roomId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody BookingRequest request) {

        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);

        return bookingService.createBooking(userId, roomId, request);
    }
}
