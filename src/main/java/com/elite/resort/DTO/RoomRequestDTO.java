package com.elite.resort.DTO;

import lombok.Data;

@Data
public class RoomRequestDTO {

    private String roomNumber;
    private String type;          // Deluxe, Suite, Standard
    private double pricePerNight;
    private int capacity;
}