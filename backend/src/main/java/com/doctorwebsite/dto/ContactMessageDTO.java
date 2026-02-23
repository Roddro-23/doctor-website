package com.doctorwebsite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for contact form submissions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    @NotBlank(message = "Message is required")
    private String message;
}
