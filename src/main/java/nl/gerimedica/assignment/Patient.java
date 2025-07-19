package nl.gerimedica.assignment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entity representing a patient in the hospital system.
 * <p>
 * Improvements:
 * - Fields are private, accessible via getters/setters
 * - Field-level validation and database constraints
 * - Appointments collection is managed and unmodifiable externally
 * - Equals/hashCode uses the primary key (id)
 * </p>
 */
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Patient name must not be blank")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "SSN must not be blank")
    @Column(nullable = false, unique = true, length = 11)
    private String ssn;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    /** Default constructor for JPA. */
    public Patient() {}

    public Patient(String name, String ssn) {
        this.name = name;
        this.ssn = ssn;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }

    /**
     * Returns the patient's appointments as an unmodifiable list.
     */
    public List<Appointment> getAppointments() {
        return appointments == null ? Collections.emptyList() : Collections.unmodifiableList(appointments);
    }
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    /**
     * JPA entity equality is best defined on the primary key after persistence.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return id != null && id.equals(patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /** For logging/debugging. */
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ssn='" + ssn + '\'' +
                '}';
    }
}
