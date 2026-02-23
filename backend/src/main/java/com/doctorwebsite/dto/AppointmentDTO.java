package com.doctorwebsite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating a new appointment booking.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+\\-\\s]{7,15}$", message = "Invalid phone number format")
    private String phone;

    private String patientEmail;

    @NotNull(message = "Appointment date and time is required")
    private LocalDateTime appointmentDatetime;

    private String reason;
}
