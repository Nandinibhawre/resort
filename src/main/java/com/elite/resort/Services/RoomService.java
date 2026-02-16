package com.elite.resort.Services;

import com.elite.resort.Exceptions.RoomUnavailableException;
import com.elite.resort.Model.Room;
import com.elite.resort.Repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepo roomRepository;

    // ✅ Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // ✅ Get room by id
    public Room getRoomById(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id " + id));
    }

    // ✅ Check availability before booking
    public Room getAvailableRoom(String id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id " + id));

        if (!room.isAvailable()) {
            throw new RoomUnavailableException("Room is currently unavailable for booking");
        }

        return room;
    }
}
