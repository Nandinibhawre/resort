package com.elite.resort.Controller;

import com.elite.resort.DTO.RoomResponseDTO;
import com.elite.resort.Model.Room;
import com.elite.resort.Services.AdminRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rooms")
@RequiredArgsConstructor
public class AdminRoomController {

    private final AdminRoomService adminRoomService;

    @PostMapping(value="/add", consumes = {"multipart/form-data"})
    public ResponseEntity<RoomResponseDTO> addRoom(

            @RequestPart("data") String data,
            @RequestPart(value="roomImage", required=false) MultipartFile roomImage

    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Room room = mapper.readValue(data, Room.class);

        return ResponseEntity.ok(adminRoomService.addRoom(room, roomImage));
    }

    // ✅ UPDATE ROOM
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<RoomResponseDTO> updateRoom(

            @PathVariable String id,
            @RequestPart("data") String data,
            @RequestPart(value = "roomImage", required = false) MultipartFile roomImage

    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        Room room = mapper.readValue(data, Room.class);

        return ResponseEntity.ok(adminRoomService.updateRoom(id, room, roomImage));
    }

    // ✅ DELETE ROOM
    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable String id) {
        return adminRoomService.deleteRoom(id);
    }

    // ✅ GET ALL ROOMS (Image auto fetched)
    @GetMapping
    public List<RoomResponseDTO> getAllRooms() {
        return adminRoomService.getAllRooms();
    }

    // ✅ GET ROOM BY ID (Image auto fetched)
    @GetMapping("/{id}")
    public RoomResponseDTO getRoom(@PathVariable String id) {
        return adminRoomService.getRoom(id);
    }

    // ✅ SET ROOM AVAILABILITY
    @PatchMapping("/{id}/availability")
    public Room setAvailability(@PathVariable String id,
                                @RequestParam boolean status) {
        return adminRoomService.setAvailability(id, status);
    }
}

