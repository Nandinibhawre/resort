package com.elite.resort.Services;

import com.elite.resort.DTO.RoomResponseDTO;
import com.elite.resort.Model.*;
import com.elite.resort.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRoomService {

    private final RoomRepo roomRepository;
    private  final S3Service s3Service;
    private  final ImageRepo imageRepository;

    public RoomResponseDTO addRoom(Room room, MultipartFile roomImage) {

        room.setAvailable(true);

        // 1️⃣ Save room first to generate roomId
        Room savedRoom = roomRepository.save(room);

        // 2️⃣ Upload image if present
        if (roomImage != null && !roomImage.isEmpty()) {

            Image image = s3Service.uploadFile(roomImage, "roomImages");

            // 3️⃣ Save image info
            image.setRoomId(savedRoom.getRoomId());
            image.setType("ROOM");

            imageRepository.save(image);

            // 4️⃣ Update room with imageUrl
            savedRoom.setImageUrl(image.getImageUrl());

            savedRoom = roomRepository.save(savedRoom);
        }

        return convertToDTO(savedRoom);
    }

    // ✅ UPDATE ROOM
    public RoomResponseDTO updateRoom(String id, Room updatedRoom, MultipartFile roomImage) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setType(updatedRoom.getType());
        room.setPricePerNight(updatedRoom.getPricePerNight());
        room.setCapacity(updatedRoom.getCapacity());
        room.setRoomNumber(updatedRoom.getRoomNumber());
        room.setAvailable(updatedRoom.isAvailable());
        if (roomImage != null && !roomImage.isEmpty()) {

            System.out.println("Uploading room image...");

            Image image = s3Service.uploadFile(roomImage, "roomImages");

            System.out.println("Image URL: " + image.getImageUrl());

            room.setImageUrl(image.getImageUrl());
        }

        Room saved = roomRepository.save(room);

        return convertToDTO(saved);
    }
    // ✅ DELETE ROOM
    public String deleteRoom(String id) {

        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found");
        }

        roomRepository.deleteById(id);
        return "Room deleted successfully";
    }

    // ✅ GET ALL ROOMS (with image auto fetched)
    public List<RoomResponseDTO> getAllRooms() {

        List<Room> rooms = roomRepository.findAll();
        List<RoomResponseDTO> responseList = new ArrayList<>();

        for (Room room : rooms) {

            RoomResponseDTO dto = convertToDTO(room);
            responseList.add(dto);
        }

        return responseList;
    }

    // ✅ GET ROOM BY ID (with image auto fetched)
    public RoomResponseDTO getRoom(String id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return convertToDTO(room);
    }

    // ✅ SET AVAILABILITY
    public Room setAvailability(String id, boolean status) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setAvailable(status);

        return roomRepository.save(room);
    }

    // 🔥 IMPORTANT: AUTO IMAGE FETCH USING ROOM NUMBER
    private RoomResponseDTO convertToDTO(Room room) {

        RoomResponseDTO dto = new RoomResponseDTO();

        dto.setRoomId(room.getRoomId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setCapacity(room.getCapacity());
        dto.setAvailable(room.isAvailable());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setUpdatedAt(room.getUpdatedAt());

//        // 🔥 Fetch image automatically using roomNumber
//        Image image = imageRepo.findByRoomNumber(room.getRoomNumber())
//                .orElse(null);
//
//        if (image != null) {
//            dto.setImageUrl(image.getImageUrl());
//        } else {
//            dto.setImageUrl(null);
//        }
//        System.out.println("Room from Room table: " + room.getRoomNumber());
//
//        List<Image> allImages = imageRepo.findAll();
//        allImages.forEach(img ->
//                System.out.println("Image roomNumber: " + img.getRoomNumber())
//        );
        return dto;
    }
}