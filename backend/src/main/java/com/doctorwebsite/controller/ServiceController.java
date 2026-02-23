package com.doctorwebsite.controller;

import com.doctorwebsite.dto.ApiResponse;
import com.doctorwebsite.entity.MedicalService;
import com.doctorwebsite.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for medical services catalog.
 */
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final DoctorService doctorService;

    /**
     * Get all active services (public).
     * GET /api/services
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalService>>> getServices() {
        List<MedicalService> services = doctorService.getAllActiveServices();
        return ResponseEntity.ok(ApiResponse.success("Services retrieved", services));
    }

    /**
     * Get service by ID (public).
     * GET /api/services/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalService>> getServiceById(@PathVariable Long id) {
        MedicalService service = doctorService.getServiceById(id);
        return ResponseEntity.ok(ApiResponse.success("Service found", service));
    }
}
