package com.elite.resort.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {




    // 🔐 Brevo config from properties
    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;
    private final WebClient webClient =
            WebClient.create("https://api.brevo.com/v3");

    // =====================================================
    // ACCOUNT CREATED EMAIL
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
    // BOOKING CONFIRMATION EMAIL
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
    // PAYMENT SUCCESS EMAIL
    // =====================================================
    public void sendPaymentSuccessEmail(
            String userEmail,
            LocalDate checkIn,
            LocalDate checkOut,
            double amount
    ) {

        String htmlContent =
                "<h2>Payment Successful ✅</h2>" +
                        "<p>Your booking is now <b>CONFIRMED</b>.</p>" +
                        "<ul>" +
                        "<li><b>Check-in:</b> " + checkIn + "</li>" +
                        "<li><b>Check-out:</b> " + checkOut + "</li>" +
                        "<li><b>Total Paid:</b> ₹" + amount + "</li>" +
                        "</ul>" +
                        "<p>We look forward to hosting you at <b>Elite Resort</b>.</p>";

        sendEmail(userEmail, "Booking Confirmed – Payment Successful", htmlContent);
    }

    // =====================================================
    // COMMON EMAIL METHOD
    // =====================================================
    private void sendEmail(String toEmail, String subject, String htmlContent) {

        // ✅ validation to prevent Brevo 400 error
        if (toEmail == null || toEmail.isBlank() || !toEmail.contains("@")) {
            throw new RuntimeException("Invalid user email: " + toEmail);
        }

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "email", senderEmail,
                        "name", senderName
                ),
                "to", List.of(
                        Map.of("email", toEmail.trim())
                ),
                "subject", subject,
                "htmlContent", htmlContent
        );

        webClient.post()
                .uri("/smtp/email")
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .map(msg -> new RuntimeException("BREVO ERROR → " + msg))
                )
                .bodyToMono(String.class)
                .block();
    }
}
