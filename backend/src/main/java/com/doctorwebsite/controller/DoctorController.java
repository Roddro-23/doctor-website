package com.doctorwebsite.controller;

import com.doctorwebsite.dto.ApiResponse;
import com.doctorwebsite.dto.DoctorDTO;
import com.doctorwebsite.entity.Doctor;
import com.doctorwebsite.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for doctor profile data.
 * All endpoints are read-only public (GET).
 */
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * Get all doctors.
     * GET /api/doctors
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorDTO>>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors()
                .stream()
                .map(doctorService::mapToDTO)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved", doctors));
    }

    /**
     * Get doctor by ID.
     * GET /api/doctors/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorDTO>> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor found", doctorService.mapToDTO(doctor)));
    }
}
