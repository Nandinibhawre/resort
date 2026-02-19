package com.elite.resort.Model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}