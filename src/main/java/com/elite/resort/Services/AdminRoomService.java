package com.elite.resort.Services;

import com.elite.resort.DTO.AdminBookingView;
import com.elite.resort.DTO.RoomResponseDTO;
import com.elite.resort.Model.*;
import com.elite.resort.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AdminRoomService {

    private final RoomRepo roomRepository;
    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final ProfileRepo profileRepo;
    private final PaymentRepo paymentRepo;
    private final ImageRepo imageRepo;

    // ✅ ADD ROOM
    public RoomResponseDTO addRoom(Room room) {
        room.setAvailable(true);
        Room savedRoom = roomRepository.save(room);
        return convertToDTO(savedRoom);
    }

    // ✅ UPDATE ROOM
    public RoomResponseDTO updateRoom(String id, Room updatedRoom) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setType(updatedRoom.getType());
        room.setPricePerNight(updatedRoom.getPricePerNight());
        room.setCapacity(updatedRoom.getCapacity());
        room.setRoomNumber(updatedRoom.getRoomNumber());
        room.setAvailable(updatedRoom.isAvailable());

        Room savedRoom = roomRepository.save(room);

        return convertToDTO(savedRoom);
    }

    // ✅ DELETE ROOM
    public String deleteRoom(String id) {

        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found");
        }

        roomRepository.deleteById(id);
        return "Room deleted successfully";
    }

    // ✅ GET ALL ROOMS
    public List<RoomResponseDTO> getAllRooms() {

        List<Room> rooms = roomRepository.findAll();
        List<RoomResponseDTO> response = new ArrayList<>();

        for (Room room : rooms) {
            response.add(convertToDTO(room));
        }

        return response;
    }

    // ✅ GET ROOM BY ID
    public RoomResponseDTO getRoom(String id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return convertToDTO(room);
    }

    // ✅ SET AVAILABILITY
    public RoomResponseDTO setAvailability(String id, boolean status) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setAvailable(status);

        Room updatedRoom = roomRepository.save(room);

        return convertToDTO(updatedRoom);
    }
    private RoomResponseDTO convertToDTO(Room room) {

        RoomResponseDTO dto = new RoomResponseDTO();

        dto.setRoomId(room.getRoomId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setCapacity(room.getCapacity());
        dto.setAvailable(room.isAvailable());

        // 🔥 Fetch image using roomNumber
        Image image = imageRepo.findByRoomNumber(room.getRoomNumber());

        if (image != null) {
            dto.setImageUrl(image.getImageUrl());
        }

        return dto;
    }

    // 🔵 YOUR BOOKING METHOD REMAINS SAME (no change)
}
