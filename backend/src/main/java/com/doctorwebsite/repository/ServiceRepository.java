package com.doctorwebsite.repository;

import com.doctorwebsite.entity.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for MedicalService entity.
 */
@Repository
public interface ServiceRepository extends JpaRepository<MedicalService, Long> {

    /** Find only active services sorted by display order */
    List<MedicalService> findByActiveTrueOrderByDisplayOrderAsc();
}
