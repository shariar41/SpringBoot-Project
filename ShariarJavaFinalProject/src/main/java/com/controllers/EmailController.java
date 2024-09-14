package com.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.services.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text) {
        Map<String, String> response = new HashMap<>();
        try {
            emailService.sendEmail(to, subject, text);
            response.put("message", "Email sent successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to send email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
