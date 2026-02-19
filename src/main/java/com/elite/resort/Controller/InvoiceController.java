package com.elite.resort.Controller;

import com.elite.resort.Model.Invoice;
import com.elite.resort.Services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    // Get invoice by booking ID
    @GetMapping("/{bookingId}")
    public Invoice getInvoice(@PathVariable String bookingId) {
        return invoiceService.getByBookingId(bookingId);
    }
}