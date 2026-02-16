package com.elite.resort.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rooms")
@Data

public class Room {

    @Id
    private String roomId;

    private String roomNumber;
    private String type; // Deluxe, Suite, Standard
    private double pricePerNight;
    private int capacity;
    private boolean available;
}