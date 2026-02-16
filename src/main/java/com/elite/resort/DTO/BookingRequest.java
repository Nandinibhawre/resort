package com.elite.resort.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    // Make tutorial
    private String roomId;
    private String userId;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
