package com.doctorwebsite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Doctor entity — stores doctor profile information.
 */
@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Doctor name is required")
    @Column(nullable = false)
    private String name;

    /** e.g. MBBS, MD, PhD */
    @Column(nullable = false)
    private String degree;

    /** e.g. Cardiology, General Medicine */
    @Column(nullable = false)
    private String specialization;

    /** Years of professional experience */
    @Column(nullable = false)
    private Integer experienceYears;

    /** Clinic working hours e.g. Mon-Fri 9AM-5PM */
    @Column(name = "clinic_timing")
    private String clinicTiming;

    /** URL or path to profile photo */
    @Column(name = "photo_url")
    private String photoUrl;

    /** Short bio / description */
    @Column(columnDefinition = "TEXT")
    private String bio;

    /** Clinic name */
    @Column(name = "clinic_name")
    private String clinicName;

    /** Consultation fee */
    @Column(name = "consultation_fee")
    private Double consultationFee;
}
