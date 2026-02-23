package com.doctorwebsite.repository;

import com.doctorwebsite.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Appointment entity — provides JPA CRUD operations.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /** Find appointments by patient name (case-insensitive) */
    List<Appointment> findByPatientNameContainingIgnoreCase(String patientName);

    /** Find appointments by status */
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);

    /** Find appointments by phone number */
    List<Appointment> findByPhone(String phone);
}
