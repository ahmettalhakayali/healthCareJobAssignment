package nl.gerimedica.assignment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Entity representing a patient's medical appointment.
 *
 * Improvements:
 * - Fields are private for encapsulation.
 * - Getters and setters provided for Spring Data/JPA compatibility.
 * - Validation annotations added for input integrity.
 * - Equals/hashCode use 'id' field, per JPA standard.
 * - toString provided for debugging/logging.
 */
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Reason is required")
    @Column(nullable = false)
    private String reason;

    @NotBlank(message = "Date is required")
    @Column(nullable = false, length = 10)
    private String date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient is required")
    private Patient patient;

    /** Default constructor for JPA. */
    public Appointment() {}

    public Appointment(String reason, String date, Patient patient) {
        this.reason = reason;
        this.date = date;
        this.patient = patient;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    /**
     * JPA best practice: Use ID for equality.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        // if either ID is null, consider not equal (not persisted)
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /** For logging/debugging. */
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", reason='" + reason + '\'' +
                ", date='" + date + '\'' +
                ", patient=" + (patient != null ? patient.getId() : null) +
                '}';
    }
}
