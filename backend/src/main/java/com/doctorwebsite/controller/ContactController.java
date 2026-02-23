package com.doctorwebsite.controller;

import com.doctorwebsite.dto.ApiResponse;
import com.doctorwebsite.dto.ContactMessageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for contact form submissions.
 * Messages are logged (can be extended to send emails using JavaMailSender).
 */
@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Slf4j
public class ContactController {

    /**
     * Submit a contact message.
     * POST /api/contact
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> submitContact(
            @Valid @RequestBody ContactMessageDTO dto) {

        // Log the contact message (extend: send email via JavaMailSender)
        log.info("Contact message from {} <{}> | Phone: {} | Message: {}",
                dto.getName(), dto.getEmail(), dto.getPhone(), dto.getMessage());

        return ResponseEntity.ok(
                ApiResponse.success("Thank you for contacting us! We'll get back to you within 24 hours.")
        );
    }
}
