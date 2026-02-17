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

    @PostMapping("/pay")
    public ResponseEntity<Payment> makePayment(@RequestBody PaymentRequest request) {

        Payment payment = paymentService.makePayment(
                request.getBookingId(),
                request.getMethod(),
                request.getTransactionId()

        );

        return ResponseEntity.ok(payment);
    }
}