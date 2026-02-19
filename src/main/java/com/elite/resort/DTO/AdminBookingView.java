package com.elite.resort.DTO;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminBookingView {

    // Booking
    private String bookingId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalAmount;
    private String bookingStatus;

    // User
    private String userName;
    private String userEmail;

    // Profile
    private String phone;
    private String address;
    private String idProof;

    // Room
    private String roomNumber;
    private String roomType;

    // Payment
    private String paymentId;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentDate;
}
