package nl.gerimedica.assignment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for handling hospital-related business logic.
 *
 * Improvements:
 * - Uses constructor injection
 * - Leverages repository query methods for performance
 * - Encapsulates entity access
 * - Adds logging and input validation
 */
@Service
@Slf4j
public class HospitalService {

    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;

    public HospitalService(PatientRepository patientRepo, AppointmentRepository appointmentRepo) {
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
    }

    /**
     * Bulk create appointments for a patient.
     * Creates the patient if not found.
     * Saves all appointments in a single batch.
     * 
     * @param patientName the patient's name
     * @param ssn the patient's SSN
     * @param reasons list of appointment reasons
     * @param dates list of appointment dates
     * @return list of created appointment DTOs
     */
    @Transactional
    public List<AppointmentDto> bulkCreateAppointments(
            String patientName,
            String ssn,
            List<String> reasons,
            List<String> dates
    ) {
        // Validate input
        if (reasons == null || dates == null || reasons.isEmpty() || dates.isEmpty()) {
            throw new IllegalArgumentException("Reasons and dates must not be empty");
        }

        Patient patient = patientRepo.findBySsn(ssn)
                .orElseGet(() -> {
                    log.info("Creating new patient with SSN: {}", ssn);
                    return patientRepo.save(new Patient(patientName, ssn));
                });

        int count = Math.min(reasons.size(), dates.size());
        List<Appointment> appointments = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            appointments.add(new Appointment(reasons.get(i), dates.get(i), patient));
        }

        List<Appointment> savedAppointments = appointmentRepo.saveAll(appointments);

        savedAppointments.forEach(appt ->
            log.info("Created appointment for reason: {} [Date: {}] [Patient SSN: {}]", 
                appt.getReason(), appt.getDate(), appt.getPatient().getSsn())
        );

        // Convert to DTOs
        List<AppointmentDto> appointmentDtos = savedAppointments.stream()
            .map(appt -> new AppointmentDto(
                appt.getId(),
                appt.getReason(),
                appt.getDate(),
                appt.getPatient().getId()
            ))
            .collect(Collectors.toList());

        // NOTE: Ideally, usage recording should be an aspect/event, not called directly.
        HospitalUtils.recordUsage("Bulk create appointments");

        return appointmentDtos;
    }

    /**
     * Find a patient by SSN.
     * @param ssn patient's SSN
     * @return Optional containing the patient if found
     */
    public Optional<Patient> findPatientBySSN(String ssn) {
        return patientRepo.findBySsn(ssn);
    }

    /**
     * Get all appointments whose reason matches the keyword (case-insensitive, contains).
     * 
     * @param reasonKeyword the keyword to search for in appointment reasons
     * @return list of matching appointment DTOs
     */
    public List<AppointmentDto> getAppointmentsByReason(String reasonKeyword) {
        // Use custom repository method for filtering in DB, not in memory!
        List<Appointment> matched = appointmentRepo.findByReasonContainingIgnoreCase(reasonKeyword);
        log.info("Found {} appointments matching reason: '{}'", matched.size(), reasonKeyword);

        // Convert to DTOs
        List<AppointmentDto> appointmentDtos = matched.stream()
            .map(appt -> new AppointmentDto(
                appt.getId(),
                appt.getReason(),
                appt.getDate(),
                appt.getPatient().getId()
            ))
            .collect(Collectors.toList());

        // Example usage tracking; in real life, use event/aspect instead
        HospitalUtils.recordUsage("Get appointments by reason");
        return appointmentDtos;
    }

    /**
     * Delete all appointments for a given patient's SSN.
     * 
     * @param ssn the patient's SSN
     * @return true if appointments were deleted, false if patient not found
     */
    @Transactional
    public boolean deleteAppointmentsBySSN(String ssn) {
        Optional<Patient> patientOpt = findPatientBySSN(ssn);
        if (patientOpt.isPresent()) {
            List<Appointment> appointments = appointmentRepo.findByPatientSsn(ssn);
            appointmentRepo.deleteAll(appointments);
            log.info("Deleted {} appointments for patient SSN: {}", 
            appointments.size(), ssn);
        return true;
    } else {
            log.warn("No patient found with SSN: {}", ssn);
            return false;
        }
    }

    /**
     * Find the latest appointment by SSN, using the appointment date.
     * 
     * @param ssn the patient's SSN
     * @return Optional containing the latest appointment DTO if found
     */
    public Optional<AppointmentDto> findLatestAppointmentBySSN(String ssn) {
        Optional<Patient> patientOpt = patientRepo.findBySsnWithAppointments(ssn);
        if (patientOpt.isEmpty()) {
            log.warn("No patient found with SSN: {}", ssn);
            return Optional.empty();
        }
        
        Patient patient = patientOpt.get();
        if (patient.getAppointments().isEmpty()) {
            log.warn("No appointments found for patient SSN: {}", ssn);
            return Optional.empty();
        }
        
        Appointment latestAppointment = patient.getAppointments().stream()
                .max(Comparator.comparing(Appointment::getDate))
                .orElse(null);
                
        if (latestAppointment == null) {
            return Optional.empty();
        }
        
        AppointmentDto appointmentDto = new AppointmentDto(
            latestAppointment.getId(),
            latestAppointment.getReason(),
            latestAppointment.getDate(),
            latestAppointment.getPatient().getId()
        );
        
        return Optional.of(appointmentDto);
    }
}
