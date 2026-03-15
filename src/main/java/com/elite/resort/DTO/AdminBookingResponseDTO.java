package com.elite.resort.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdminBookingResponseDTO
{
    private String bookingId;

    private String userId;
    private String userName;
    private String userEmail;

    private String roomId;
    private String roomNumber;
    private String roomType;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private double totalAmount;
    private String status;

    private String paymentId;
    private String paymentStatus;
    private String paymentMethod;

    private LocalDateTime createdAt;
}
