package com.elite.resort.Controller;

import com.elite.resort.Model.Room;
import com.elite.resort.Services.AdminRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/rooms")
@RequiredArgsConstructor
public class AdminRoomController {

    private final AdminRoomService adminRoomService;

    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return adminRoomService.addRoom(room);
    }

    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable String id,
                           @RequestBody Room room) {
        return adminRoomService.updateRoom(id, room);
    }

    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable String id) {
        return adminRoomService.deleteRoom(id);
    }

    // optional admin endpoints

    @GetMapping
    public Object getAllRooms() {
        return adminRoomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable String id) {
        return adminRoomService.getRoom(id);
    }
}
