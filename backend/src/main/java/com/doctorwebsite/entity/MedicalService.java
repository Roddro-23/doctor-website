package com.doctorwebsite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MedicalService entity — represents a medical service offered by the clinic.
 */
@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Service name is required")
    @Column(nullable = false, unique = true)
    private String name;

    /** Short description shown on the Services page */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** FontAwesome icon class e.g. fa-heart, fa-tooth */
    @Column(name = "icon_class")
    private String iconClass;

    /** Display order on the services page */
    @Column(name = "display_order")
    private Integer displayOrder = 0;

    /** Whether this service is currently active */
    @Column(nullable = false)
    private Boolean active = true;
}
