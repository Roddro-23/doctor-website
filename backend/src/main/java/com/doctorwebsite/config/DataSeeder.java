package com.doctorwebsite.config;

import com.doctorwebsite.entity.Doctor;
import com.doctorwebsite.entity.MedicalService;
import com.doctorwebsite.repository.DoctorRepository;
import com.doctorwebsite.repository.ServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * DataSeeder — seeds the database with initial doctor and service data
 * when the application first starts (only if tables are empty).
 */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(DoctorRepository doctorRepo, ServiceRepository serviceRepo) {
        return args -> {

            // Seed doctor only if none exist
            if (doctorRepo.count() == 0) {
                Doctor doctor = new Doctor();
                doctor.setName("Dr. Sarah Johnson");
                doctor.setDegree("MBBS, MD (Cardiology), FCPS");
                doctor.setSpecialization("Cardiology & General Medicine");
                doctor.setExperienceYears(15);
                doctor.setClinicTiming("Mon - Fri: 9:00 AM – 6:00 PM | Sat: 10:00 AM – 2:00 PM");
                doctor.setPhotoUrl("https://img.freepik.com/free-photo/woman-doctor-wearing-lab-coat-with-stethoscope-isolated_1303-29791.jpg");
                doctor.setBio("Dr. Sarah Johnson is a board-certified cardiologist and general physician with over 15 years of clinical experience. She is dedicated to providing compassionate, evidence-based care tailored to each patient's needs. She completed her MD from Dhaka Medical College and fellowship training in Cardiology from NICVD.");
                doctor.setClinicName("HealthCare Clinic");
                doctor.setConsultationFee(800.0);
                doctorRepo.save(doctor);
            }

            // Seed services only if none exist
            if (serviceRepo.count() == 0) {
                List<MedicalService> services = List.of(
                    createService("Cardiology", "Expert diagnosis and treatment for heart conditions including ECG, echocardiography, and cardiac consultation.", "fa-heart", 1),
                    createService("General Medicine", "Comprehensive primary care for adults including routine check-ups, chronic disease management, and preventive care.", "fa-stethoscope", 2),
                    createService("Surgery", "Skilled surgical procedures performed in a sterile environment with advanced post-operative care and monitoring.", "fa-syringe", 3),
                    createService("Dental Care", "Complete oral health services including teeth cleaning, fillings, extractions, and cosmetic dentistry.", "fa-tooth", 4),
                    createService("Pediatrics", "Specialized healthcare for infants, children, and adolescents including vaccinations and developmental assessments.", "fa-baby", 5),
                    createService("Orthopedics", "Diagnosis and treatment of musculoskeletal conditions including fractures, joint pain, and sports injuries.", "fa-bone", 6),
                    createService("Neurology", "Evaluation and management of neurological conditions including migraines, seizures, and nerve disorders.", "fa-brain", 7),
                    createService("Dermatology", "Skin care services including treatment of acne, eczema, psoriasis, and cosmetic skin procedures.", "fa-user-md", 8)
                );
                serviceRepo.saveAll(services);
            }
        };
    }

    private MedicalService createService(String name, String description, String icon, int order) {
        MedicalService s = new MedicalService();
        s.setName(name);
        s.setDescription(description);
        s.setIconClass(icon);
        s.setDisplayOrder(order);
        s.setActive(true);
        return s;
    }
}
