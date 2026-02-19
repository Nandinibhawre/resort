package com.elite.resort.Services;

import com.elite.resort.DTO.AdminBookingView;
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
    public List<AdminBookingView> getAllBookingsForAdmin() {

        List<Booking> bookings = bookingRepo.findAll();
        List<AdminBookingView> response = new ArrayList<>();

        for (Booking booking : bookings) {

            AdminBookingView view = new AdminBookingView();

            // BOOKING
            view.setBookingId(booking.getBookingId());
            view.setCheckIn(booking.getCheckIn());
            view.setCheckOut(booking.getCheckOut());
            view.setTotalAmount(booking.getTotalAmount());
            view.setBookingStatus(booking.getStatus());

            // USER
            User user = userRepo.findById(booking.getUserId()).orElse(null);
            if (user != null) {
                view.setUserName(user.getName());
                view.setUserEmail(user.getEmail());
            }

            // PROFILE
            Profile profile = profileRepo.findByUserId(booking.getUserId()).orElse(null);
            if (profile != null) {
                view.setPhone(profile.getPhone());
                view.setAddress(profile.getAddress());
                view.setIdProof(profile.getIdProof());
            }

            // ROOM
            Room room = roomRepository.findById(booking.getRoomId()).orElse(null);
            if (room != null) {
                view.setRoomNumber(room.getRoomNumber());
                view.setRoomType(room.getType());
            }

            // PAYMENT
            Payment payment = paymentRepo.findByBookingId(booking.getBookingId()).orElse(null);
            if (payment != null) {
                view.setPaymentId(payment.getPaymentId());
                view.setPaymentMethod(payment.getMethod());
                view.setPaymentStatus(payment.getStatus());
                view.setPaymentDate(payment.getPaidAt());
            }

            response.add(view);
        }

        return response;
    }

}
