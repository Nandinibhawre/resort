package com.elite.resort.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;

    // 🔐 Brevo config from properties
    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    // 🌐 Brevo WebClient
    private final WebClient webClient =
            WebClient.create("https://api.brevo.com/v3");

    // =====================================================
    // ✅ 1. ACCOUNT CREATED EMAIL
    // =====================================================
    public void sendAccountCreatedEmail(String toEmail, String name) {

        String html =
                "<h2>Welcome to Elite Resort 🏨</h2>" +
                        "<p>Hello <b>" + name + "</b>,</p>" +
                        "<p>Your account has been created successfully.</p>" +
                        "<p>You can now login and book rooms easily.</p>" +
                        "<br>" +
                        "<p>Thank you for choosing us ❤️</p>";

        sendEmail(toEmail, "Welcome to Elite Resort", html);
    }

    // =====================================================
    // ✅ 2. BOOKING CONFIRMATION EMAIL
    // =====================================================
    public void sendBookingConfirmationEmail(
            String toEmail,
            String name,
            String room,
            LocalDate checkIn,
            LocalDate checkOut) {

        String html =
                "<h2>Booking Confirmed</h2>" +
                        "<p>Hello <b>" + name + "</b>,</p>" +
                        "<p>Your room has been booked successfully.</p>" +
                        "<p><b>Room:</b> " + room + "</p>" +
                        "<p><b>Check-in:</b> " + checkIn + "</p>" +
                        "<p><b>Check-out:</b> " + checkOut + "</p>" +
                        "<p>Thank you for choosing Elite Resort ❤️</p>";

        sendEmail(toEmail, "Booking Confirmed - Elite Resort", html);
    }

    // =====================================================
    // ✅ COMMON EMAIL METHOD (BREVO API)
    // =====================================================
    private void sendEmail(String toEmail, String subject, String htmlContent) {

        webClient.post()
                .uri("/smtp/email")
                .header("api-key", apiKey)
                .bodyValue(Map.of(
                        "sender", Map.of(
                                "email", senderEmail,
                                "name", senderName
                        ),
                        "to", new Object[]{
                                Map.of("email", toEmail)
                        },
                        "subject", subject,
                        "htmlContent", htmlContent
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    // ================= BOOKING CONFIRMATION AFTER PAYMENT =================
    public void sendPaymentSuccessEmail(
            String userEmail,
            String roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            double amount
    ) {

        String subject = "Booking Confirmed – Payment Successful";

        String message = "Dear Customer,\n\n"
                + "Your payment was successful and your booking is now CONFIRMED.\n\n"
                + "Booking Details:\n"
                + "Room ID: " + roomId + "\n"
                + "Check-in Date: " + checkIn + "\n"
                + "Check-out Date: " + checkOut + "\n"
                + "Total Amount Paid: ₹" + amount + "\n\n"
                + "We look forward to hosting you.\n"
                + "Thank you for choosing our resort!\n\n"
                + "Best Regards,\n"
                + "Elite Resort Team";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
    }
}
