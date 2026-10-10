package com.hospital.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.backend.model.Appointment;
import com.hospital.backend.model.Billing;
import com.hospital.backend.model.Doctor;
import com.hospital.backend.model.Patient;
import com.hospital.backend.repository.AppointmentRepository;
import com.hospital.backend.repository.BillingRepository;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.PatientRepository;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class HospitalController {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillingRepository billingRepository;

    public HospitalController(PatientRepository patientRepository,
                             DoctorRepository doctorRepository,
                             AppointmentRepository appointmentRepository,
                             BillingRepository billingRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.billingRepository = billingRepository;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/patients")
    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        Patient savedPatient = patientRepository.save(patient);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @GetMapping("/doctors")
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @PostMapping("/doctors")
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody Doctor doctor) {
        Doctor savedDoctor = doctorRepository.save(doctor);
        return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
    }

    @GetMapping("/appointments")
    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Map<String, Object> payload) {
        Long patientId = payload.get("patientId") != null ? Long.valueOf(payload.get("patientId").toString()) : null;
        Long doctorId = payload.get("doctorId") != null ? Long.valueOf(payload.get("doctorId").toString()) : null;

        if (patientId == null || doctorId == null) {
            return ResponseEntity.badRequest().build();
        }

        Patient patient = patientRepository.findById(patientId)
                .orElse(null);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElse(null);

        if (patient == null || doctor == null) {
            return ResponseEntity.notFound().build();
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(LocalDate.parse(payload.getOrDefault("appointmentDate", LocalDate.now()).toString()));
        appointment.setTimeSlot(payload.getOrDefault("timeSlot", "09:00 AM").toString());
        appointment.setStatus(payload.getOrDefault("status", "Scheduled").toString());

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/billing")
    public List<Billing> getBilling() {
        return billingRepository.findAll();
    }

    @PostMapping("/billing")
    public ResponseEntity<Billing> createBilling(@RequestBody Map<String, Object> payload) {
        Long patientId = payload.get("patientId") != null ? Long.valueOf(payload.get("patientId").toString()) : null;
        if (patientId == null) {
            return ResponseEntity.badRequest().build();
        }

        Patient patient = patientRepository.findById(patientId).orElse(null);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }

        Billing billing = new Billing();
        billing.setPatient(patient);
        billing.setAmount(Double.valueOf(payload.getOrDefault("amount", 0).toString()));
        billing.setBillingType(payload.getOrDefault("billingType", "Consultation").toString());
        billing.setStatus(payload.getOrDefault("status", "Pending").toString());
        billing.setBillingDate(LocalDate.parse(payload.getOrDefault("billingDate", LocalDate.now()).toString()));

        Billing savedBilling = billingRepository.save(billing);
        return new ResponseEntity<>(savedBilling, HttpStatus.CREATED);
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = new java.util.HashMap<>();
        dashboard.put("totalPatients", patientRepository.count());
        dashboard.put("totalDoctors", doctorRepository.count());
        dashboard.put("totalAppointments", appointmentRepository.count());
        dashboard.put("totalBilling", billingRepository.count());
        dashboard.put("date", LocalDate.now().toString());
        return dashboard;
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
