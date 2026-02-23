package com.doctorwebsite.controller;

import com.doctorwebsite.dto.ApiResponse;
import com.doctorwebsite.dto.AppointmentDTO;
import com.doctorwebsite.entity.Appointment;
import com.doctorwebsite.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for appointment operations.
 * Public: POST /api/appointments (book)
 * Admin:  GET, PUT, DELETE (require admin password header)
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // ==================== PUBLIC ====================

    /**
     * Book a new appointment.
     * POST /api/appointments
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Appointment>> bookAppointment(
            @Valid @RequestBody AppointmentDTO dto) {

        Appointment appointment = appointmentService.bookAppointment(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment booked successfully!", appointment));
    }

    // ==================== ADMIN ====================

    /**
     * Get all appointments — Admin only.
     * GET /api/appointments?adminPassword=xxxx
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Appointment>>> getAllAppointments(
            @RequestParam String adminPassword) {

        validateAdmin(adminPassword);
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved", appointments));
    }

    /**
     * Get appointment by ID — Admin only.
     * GET /api/appointments/{id}?adminPassword=xxxx
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Appointment>> getById(
            @PathVariable Long id,
            @RequestParam String adminPassword) {

        validateAdmin(adminPassword);
        Appointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment found", appointment));
    }

    /**
     * Update appointment status — Admin only.
     * PUT /api/appointments/{id}/status?adminPassword=xxxx
     * Body: { "status": "CONFIRMED" }
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Appointment>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestParam String adminPassword) {

        validateAdmin(adminPassword);
        String status = body.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Status field is required"));
        }
        Appointment updated = appointmentService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status updated to " + status, updated));
    }

    /**
     * Delete an appointment — Admin only.
     * DELETE /api/appointments/{id}?adminPassword=xxxx
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAppointment(
            @PathVariable Long id,
            @RequestParam String adminPassword) {

        validateAdmin(adminPassword);
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment deleted successfully"));
    }

    /**
     * Simple in-request admin password check.
     * For production, replace with JWT-based authentication.
     */
    private void validateAdmin(String password) {
        // The admin password is configured in application.properties
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        if (adminPassword == null || adminPassword.isBlank()) {
            adminPassword = "admin123"; // fallback for local dev
        }
        if (!adminPassword.equals(password)) {
            throw new IllegalArgumentException("Unauthorized: invalid admin password.");
        }
    }
}
