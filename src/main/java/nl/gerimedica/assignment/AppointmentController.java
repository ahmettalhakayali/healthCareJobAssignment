package nl.gerimedica.assignment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing appointments.
 * 
 * Improvements:
 * - Constructor injection for testability
 * - Uses DTOs for request and response payloads
 * - Proper REST conventions for endpoints and HTTP methods
 * - Input validation with @Valid and constraint annotations
 * - Consistent API versioning
 * - Error handling for not-found cases
 */
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final HospitalService hospitalService;

    public AppointmentController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    /**
     * Bulk create appointments for a patient.
     * Example payload:
     * {
     *   "reasons": ["Checkup", "Follow-up", "X-Ray"],
     *   "dates": ["2025-02-01", "2025-02-15", "2025-03-01"]
     * }
     * 
     * @param patientName the patient's name
     * @param ssn the patient's SSN
     * @param payload the bulk appointment request
     * @return list of created appointment DTOs
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<AppointmentDto>> createBulkAppointments(
            @RequestParam @NotBlank String patientName,
            @RequestParam @NotBlank String ssn,
            @Valid @RequestBody BulkAppointmentRequest payload) {

        List<AppointmentDto> created = hospitalService.bulkCreateAppointments(
            patientName, ssn, payload.getReasons(), payload.getDates());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Find appointments by keyword in reason.
     * 
     * @param keyword the keyword to search for in appointment reasons
     * @return list of matching appointment DTOs
     */
    @GetMapping("/search")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByReason(
            @RequestParam @NotBlank String keyword) {
        List<AppointmentDto> found = hospitalService.getAppointmentsByReason(keyword);
        return ResponseEntity.ok(found);
    }

    /**
     * Delete all appointments for a specific patient by SSN.
     * 
     * @param ssn the patient's SSN
     * @return 204 No Content if successful, 404 if patient not found
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAppointmentsBySSN(@RequestParam @NotBlank String ssn) {
        boolean deleted = hospitalService.deleteAppointmentsBySSN(ssn);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get the latest appointment for a patient.
     * 
     * @param ssn the patient's SSN
     * @return the latest appointment DTO if found, 404 if not found
     */
    @GetMapping("/latest")
    public ResponseEntity<AppointmentDto> getLatestAppointment(@RequestParam @NotBlank String ssn) {
        return hospitalService.findLatestAppointmentBySSN(ssn)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
