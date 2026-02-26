package com.elite.resort.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDTO
{
    private String roomId;
    private String roomNumber;
    private String type;
    private double pricePerNight;
    private int capacity;
    private boolean available;

    // 👇 This will be fetched using imageId
    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
