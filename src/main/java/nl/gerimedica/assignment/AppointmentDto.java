package nl.gerimedica.assignment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for appointment details returned by API.
*/
public class AppointmentDto {
    
    @NotNull(message = "Appointment ID is required")
    private Long id;
    
    @NotBlank(message = "Reason is required")
    private String reason;
    
    @NotBlank(message = "Date is required")
    private String date;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    // Default constructor for JSON deserialization
    public AppointmentDto() {}
    
    public AppointmentDto(Long id, String reason, String date, Long patientId) {
        this.id = id;
        this.reason = reason;
        this.date = date;
        this.patientId = patientId;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
} 