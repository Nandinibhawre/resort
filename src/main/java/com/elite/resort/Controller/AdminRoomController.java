package com.elite.resort.Controller;

import com.elite.resort.DTO.AdminBookingView;
import com.elite.resort.Model.Room;

import com.elite.resort.Repository.RoomRepo;
import com.elite.resort.Services.AdminRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rooms")
@RequiredArgsConstructor
public class AdminRoomController {

    private final AdminRoomService adminRoomService;

    //Adding rooms
    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return adminRoomService.addRoom(room);
    }

    //adding rooms
    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable String id,
                           @RequestBody Room room) {
        return adminRoomService.updateRoom(id, room);
    }

    //deleting rooms
    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable String id) {
        return adminRoomService.deleteRoom(id);
    }

    // get all rooms
    @GetMapping("/getallRooms")
    public Object getAllRooms() {
        return adminRoomService.getAllRooms();
    }

    //get rooms by id
    @GetMapping("/{id}")
    public Room getRoom(@PathVariable String id) {
        return adminRoomService.getRoom(id);
    }

    //see booking details
    // 🔐 Only ADMIN can access
    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminBookingView>> getAllBookings() {
        return ResponseEntity.ok(adminRoomService.getAllBookingsForAdmin());
    }

    //change the avilibity od the rooms
    @PatchMapping("/{roomId}/availability/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public Room setAvailability(@PathVariable String roomId,
                                @PathVariable boolean status) {
        return adminRoomService.setAvailability(roomId, status);
    }
}
