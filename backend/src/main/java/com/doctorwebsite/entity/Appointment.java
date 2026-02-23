package com.doctorwebsite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Appointment entity — represents a patient's appointment booking.
 */
@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Patient name is required")
    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+\\-\\s]{7,15}$", message = "Invalid phone number")
    @Column(nullable = false)
    private String phone;

    @Column(name = "patient_email")
    private String patientEmail;

    @NotNull(message = "Appointment date and time is required")
    @Column(name = "appointment_datetime", nullable = false)
    private LocalDateTime appointmentDatetime;

    /** Reason for visit */
    @Column(columnDefinition = "TEXT")
    private String reason;

    /** Appointment status */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum AppointmentStatus {
        PENDING, CONFIRMED, CANCELLED
    }
}
