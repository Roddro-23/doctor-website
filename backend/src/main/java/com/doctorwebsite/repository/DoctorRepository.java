package com.doctorwebsite.repository;

import com.doctorwebsite.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Doctor entity.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /** Search doctors by specialization */
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);
}
