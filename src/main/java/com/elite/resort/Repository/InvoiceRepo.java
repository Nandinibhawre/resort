package com.elite.resort.Repository;

 import com.elite.resort.Model.Invoice;

 import org.springframework.data.mongodb.repository.MongoRepository;

 import java.util.Optional;

 public interface InvoiceRepo extends MongoRepository<Invoice, String> {

     Optional<Invoice> findByBookingId(String bookingId);
 }