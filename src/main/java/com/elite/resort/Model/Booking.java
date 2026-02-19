package com.elite.resort.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    private String bookingId;

    private String userId;   // Only USER can book
    private String roomId;

    private LocalDate checkIn;
    private LocalDate checkOut;

    // ⭐ Automatically set when created
    @CreatedDate
    private LocalDateTime createdAt;

    // ⭐ Automatically updated on save
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private double totalAmount;
    private String status;

    private String paymentId;  // added
    private LocalDateTime paymentDoneAt;
}
