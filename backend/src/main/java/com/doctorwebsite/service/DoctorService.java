package com.doctorwebsite.service;

import com.doctorwebsite.dto.DoctorDTO;
import com.doctorwebsite.entity.Doctor;
import com.doctorwebsite.entity.MedicalService;
import com.doctorwebsite.exception.ResourceNotFoundException;
import com.doctorwebsite.repository.DoctorRepository;
import com.doctorwebsite.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for doctor and medical service operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ServiceRepository serviceRepository;

    // ==================== DOCTOR ====================

    /**
     * Get all doctors.
     */
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    /**
     * Get doctor by ID.
     */
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
    }

    /**
     * Map Doctor entity to DoctorDTO (safe outbound DTO).
     */
    public DoctorDTO mapToDTO(Doctor doctor) {
        return new DoctorDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getDegree(),
                doctor.getSpecialization(),
                doctor.getExperienceYears(),
                doctor.getClinicTiming(),
                doctor.getPhotoUrl(),
                doctor.getBio(),
                doctor.getClinicName(),
                doctor.getConsultationFee()
        );
    }

    /**
     * Save or update a doctor.
     */
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // ==================== SERVICES ====================

    /**
     * Get all active services ordered by display position.
     */
    public List<MedicalService> getAllActiveServices() {
        return serviceRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    /**
     * Get service by ID.
     */
    public MedicalService getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));
    }

    /**
     * Get all services (including inactive — for admin).
     */
    public List<MedicalService> getAllServices() {
        return serviceRepository.findAll();
    }
}
