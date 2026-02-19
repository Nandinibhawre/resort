package com.elite.resort.Repository;

import com.elite.resort.Model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentRepo extends MongoRepository<Payment, String>
{
    Optional<Payment> findByBookingId(String bookingId);
}