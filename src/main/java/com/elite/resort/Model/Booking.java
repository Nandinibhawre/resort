package com.elite.resort.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "bookings")
@Data
public class Booking {

    @Id
    private String id;

    private String userId;   // Only USER can book
    private String roomId;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private double totalAmount;
    private String status;
}
