package com.elite.resort.DTO;

import lombok.Data;

@Data
public class PaymentRequest
{
    private String bookingId;
    private String method;
    private String transactionId;
}