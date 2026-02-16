package com.elite.resort.Services;

import com.elite.resort.Model.Room;
import com.elite.resort.Repository.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminRoomService {

    private final RoomRepo roomRepository;

    // ✅ ADD ROOM
    public Room addRoom(Room room) {
        room.setAvailable(true);
        return roomRepository.save(room);
    }

    // ✅ UPDATE ROOM
    public Room updateRoom(String id, Room updatedRoom) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setType(updatedRoom.getType());
        room.setPricePerNight(updatedRoom.getPricePerNight());
        room.setCapacity(updatedRoom.getCapacity());

        // optional fields if present
        room.setRoomNumber(updatedRoom.getRoomNumber());
        room.setAvailable(updatedRoom.isAvailable());

        return roomRepository.save(room);
    }

    // ✅ DELETE ROOM
    public String deleteRoom(String id) {

        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found");
        }

        roomRepository.deleteById(id);
        return "Room deleted successfully";
    }

    // ✅ GET ALL ROOMS (admin panel useful)
    public Object getAllRooms() {
        return roomRepository.findAll();
    }

    // ✅ GET ROOM BY ID
    public Room getRoom(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }
}
