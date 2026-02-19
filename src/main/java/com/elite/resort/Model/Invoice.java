package com.elite.resort.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    private String id;

    private String bookingId;

    private double roomAmount;

    private double finalAmount;

    private String paymentStatus; // PAID / PENDING

    private LocalDateTime createdAt;
}