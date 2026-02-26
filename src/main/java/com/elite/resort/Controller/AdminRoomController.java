package com.elite.resort.Controller;

import com.elite.resort.DTO.RoomResponseDTO;
import com.elite.resort.Model.Room;
import com.elite.resort.Services.AdminRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rooms")
@RequiredArgsConstructor
public class AdminRoomController {

    private final AdminRoomService adminRoomService;

    // ✅ ADD ROOM (No imageUrl needed)
    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return adminRoomService.addRoom(room);
    }

    // ✅ UPDATE ROOM
    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable String id,
                           @RequestBody Room room) {
        return adminRoomService.updateRoom(id, room);
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

