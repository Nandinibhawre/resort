package com.elite.resort.Repository;

import com.elite.resort.Model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepo extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
}