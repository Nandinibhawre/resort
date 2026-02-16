package com.elite.resort.Repository;

import com.elite.resort.Model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepo extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);

    @Query("""
{
  "roomId": ?0,
  "status": "CONFIRMED",
  "checkIn": { "$lt": ?2 },
  "checkOut": { "$gt": ?1 }
}
""")
    List<Booking> findConflictingBookings(
            String roomId,
            LocalDate checkIn,
            LocalDate checkOut
    );
}