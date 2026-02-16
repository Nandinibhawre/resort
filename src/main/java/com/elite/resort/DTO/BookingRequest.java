package com.elite.resort.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {

    // This is DTo
    private String roomId;
    private String userId;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
