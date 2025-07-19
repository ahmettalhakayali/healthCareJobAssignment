package nl.gerimedica.assignment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * DTO for bulk appointment creation payload.
 */
public class BulkAppointmentRequest {

    @NotEmpty(message = "Reasons list must not be empty")
    @Size(min = 1, max = 100, message = "Reasons list must contain between 1 and 100 items")
    private List<String> reasons;

    @NotEmpty(message = "Dates list must not be empty")
    @Size(min = 1, max = 100, message = "Dates list must contain between 1 and 100 items")
    private List<String> dates;

    // Default constructor for JSON deserialization
    public BulkAppointmentRequest() {}

    public BulkAppointmentRequest(List<String> reasons, List<String> dates) {
        this.reasons = reasons;
        this.dates = dates;
    }

    // Getters and setters
    public List<String> getReasons() {
        return reasons;
    }
    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
    public List<String> getDates() {
        return dates;
    }
    public void setDates(List<String> dates) {
        this.dates = dates;
    }
}
