package com.doctorwebsite.service;

import com.doctorwebsite.dto.AppointmentDTO;
import com.doctorwebsite.entity.Appointment;
import com.doctorwebsite.exception.ResourceNotFoundException;
import com.doctorwebsite.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for appointment operations.
 * Contains all business logic for booking and managing appointments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    /**
     * Book a new appointment from the submitted DTO.
     * Validates that the appointment date is in the future.
     */
    public Appointment bookAppointment(AppointmentDTO dto) {
        log.info("Booking appointment for patient: {}", dto.getPatientName());

        // Business rule: appointment must be in the future
        if (dto.getAppointmentDatetime().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date must be in the future.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatientName(dto.getPatientName());
        appointment.setPhone(dto.getPhone());
        appointment.setPatientEmail(dto.getPatientEmail());
        appointment.setAppointmentDatetime(dto.getAppointmentDatetime());
        appointment.setReason(dto.getReason());
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment booked successfully with id: {}", saved.getId());
        return saved;
    }

    /**
     * Get all appointments (admin use).
     */
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * Get appointment by ID.
     */
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id));
    }

    /**
     * Update appointment status (admin action: CONFIRMED or CANCELLED).
     */
    public Appointment updateStatus(Long id, String statusStr) {
        Appointment appointment = getAppointmentById(id);

        Appointment.AppointmentStatus status;
        try {
            status = Appointment.AppointmentStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Use: PENDING, CONFIRMED, or CANCELLED");
        }

        appointment.setStatus(status);
        log.info("Updated appointment {} status to {}", id, status);
        return appointmentRepository.save(appointment);
    }

    /**
     * Delete an appointment (admin use).
     */
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment", id);
        }
        appointmentRepository.deleteById(id);
        log.info("Deleted appointment with id: {}", id);
    }

    /**
     * Find appointments by status.
     */
    public List<Appointment> getByStatus(String statusStr) {
        Appointment.AppointmentStatus status = Appointment.AppointmentStatus.valueOf(statusStr.toUpperCase());
        return appointmentRepository.findByStatus(status);
    }
}
