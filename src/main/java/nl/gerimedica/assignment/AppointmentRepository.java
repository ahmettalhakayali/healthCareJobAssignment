package nl.gerimedica.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link Appointment} entities.
 * <p>
 * Extends Spring Data JPA's {@link JpaRepository} to provide basic CRUD and paging.
 * Custom queries can be defined by method naming conventions.
 * </p>
 *
 * Example custom query: {@code findByReason(String reason)}
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Find all appointments with a specific reason.
     *
     * @param reason appointment reason (e.g. "Checkup")
     * @return list of matching appointments
     */
    List<Appointment> findByReason(String reason);

    /**
     * Find all appointments whose reason contains the given keyword (case-insensitive).
     *
     * @param reasonKeyword keyword to search for in appointment reasons
     * @return list of matching appointments
     */
    List<Appointment> findByReasonContainingIgnoreCase(String reasonKeyword);

    /**
     * Find appointments by date.
     * @param date appointment date in format "YYYY-MM-DD"
     * @return list of appointments on the specified date
     */
    List<Appointment> findByDate(String date);

    /**
     * Find appointments by patient ID.
     * @param patientId the patient's ID
     * @return list of appointments for the specified patient
     */
    List<Appointment> findByPatientId(Long patientId);

    /**
     * Find appointments by patient SSN.
     * @param patientSsn the patient's SSN
     * @return list of appointments for the specified patient
     */
    List<Appointment> findByPatientSsn(String patientSsn);

    // Add more derived or @Query methods as needed
}
