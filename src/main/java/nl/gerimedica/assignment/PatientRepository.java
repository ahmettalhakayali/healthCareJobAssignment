package nl.gerimedica.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Patient} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD, paging, and query derivation.
 * Custom queries can be declared by method signature.
 * </p>
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Find a patient by SSN.
     * @param ssn Social Security Number
     * @return an Optional of Patient, empty if not found
     */
    Optional<Patient> findBySsn(String ssn);

    /**
     * Find patients by name (case-insensitive).
     * @param name patient name
     * @return list of patients with matching name
     */
    List<Patient> findByNameIgnoreCase(String name);

    /**
     * Find patients by name containing keyword (case-insensitive).
     * @param nameKeyword keyword to search in patient names
     * @return list of patients with names containing the keyword
     */
    List<Patient> findByNameContainingIgnoreCase(String nameKeyword);

    /**
     * Check if patient exists by SSN.
     * @param ssn Social Security Number
     * @return true if patient exists, false otherwise
     */
    boolean existsBySsn(String ssn);

    /**
     * Find a patient by SSN with appointments eagerly loaded.
     * @param ssn Social Security Number
     * @return an Optional of Patient with appointments, empty if not found
     */
    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.appointments WHERE p.ssn = :ssn")
    Optional<Patient> findBySsnWithAppointments(@Param("ssn") String ssn);

    // Extend with more custom queries as needed
}
