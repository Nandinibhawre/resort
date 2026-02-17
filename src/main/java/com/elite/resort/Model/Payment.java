package com.elite.resort.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    private String bookingId;
    private double amount;

    private String method;     // UPI | CARD | NETBANKING
    private String status;     // SUCCESS | FAILED

    private String transactionId;
    private LocalDateTime paidAt;
}
