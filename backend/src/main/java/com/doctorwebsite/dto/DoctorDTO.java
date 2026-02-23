package com.doctorwebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for reading doctor profile data (outbound to frontend).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {

    private Long id;
    private String name;
    private String degree;
    private String specialization;
    private Integer experienceYears;
    private String clinicTiming;
    private String photoUrl;
    private String bio;
    private String clinicName;
    private Double consultationFee;
}
