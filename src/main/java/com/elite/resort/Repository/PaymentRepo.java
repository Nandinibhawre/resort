package com.elite.resort.Repository;

import com.elite.resort.Model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepo extends MongoRepository<Payment, String> {
}