package nl.gerimedica.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Simple unit tests for HospitalService.
 */
@ExtendWith(MockitoExtension.class)
class HospitalServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private HospitalService hospitalService;

    private Patient testPatient;
    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        // Create test data
        testPatient = new Patient("John Doe", "123-45-6789");
        testPatient.setId(1L);
        
        testAppointment = new Appointment("Checkup", "2025-01-15", testPatient);
        testAppointment.setId(1L);
    }

    @Test
    void shouldCreateNewPatientAndAppointments() {
        // Given
        String patientName = "Jane Smith";
        String ssn = "987-65-4321";
        List<String> reasons = Arrays.asList("Checkup");
        List<String> dates = Arrays.asList("2025-01-15");

        Patient newPatient = new Patient(patientName, ssn);
        newPatient.setId(2L);

        when(patientRepository.findBySsn(ssn)).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(newPatient);
        when(appointmentRepository.saveAll(any())).thenReturn(Arrays.asList(testAppointment));

        // When
        List<AppointmentDto> result = hospitalService.bulkCreateAppointments(patientName, ssn, reasons, dates);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(patientRepository).findBySsn(ssn);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void shouldUseExistingPatient() {
        // Given
        String patientName = "John Doe";
        String ssn = "123-45-6789";
        List<String> reasons = Arrays.asList("Checkup");
        List<String> dates = Arrays.asList("2025-01-15");

        when(patientRepository.findBySsn(ssn)).thenReturn(Optional.of(testPatient));
        when(appointmentRepository.saveAll(any())).thenReturn(Arrays.asList(testAppointment));

        // When
        List<AppointmentDto> result = hospitalService.bulkCreateAppointments(patientName, ssn, reasons, dates);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(patientRepository).findBySsn(ssn);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void shouldThrowExceptionForEmptyLists() {
        // Given
        String patientName = "John Doe";
        String ssn = "123-45-6789";
        List<String> reasons = Arrays.asList();
        List<String> dates = Arrays.asList();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            hospitalService.bulkCreateAppointments(patientName, ssn, reasons, dates));
    }

    @Test
    void shouldFindPatientBySSN() {
        // Given
        String ssn = "123-45-6789";
        when(patientRepository.findBySsn(ssn)).thenReturn(Optional.of(testPatient));

        // When
        Optional<Patient> result = hospitalService.findPatientBySSN(ssn);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testPatient, result.get());
    }

    @Test
    void shouldReturnEmptyWhenPatientNotFound() {
        // Given
        String ssn = "999-99-9999";
        when(patientRepository.findBySsn(ssn)).thenReturn(Optional.empty());

        // When
        Optional<Patient> result = hospitalService.findPatientBySSN(ssn);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindAppointmentsByReason() {
        // Given
        String keyword = "Checkup";
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByReasonContainingIgnoreCase(keyword)).thenReturn(appointments);

        // When
        List<AppointmentDto> result = hospitalService.getAppointmentsByReason(keyword);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAppointment.getReason(), result.get(0).getReason());
    }

    @Test
    void shouldDeleteAppointmentsWhenPatientExists() {
        // Given
        String ssn = "123-45-6789";
        testPatient.setAppointments(Arrays.asList(testAppointment));
        when(patientRepository.findBySsn(ssn)).thenReturn(Optional.of(testPatient));

        // When
        boolean result = hospitalService.deleteAppointmentsBySSN(ssn);

        // Then
        assertTrue(result);
        verify(appointmentRepository).deleteAll(testPatient.getAppointments());
    }

    @Test
    void shouldReturnFalseWhenPatientNotFoundForDeletion() {
        // Given
        String ssn = "999-99-9999";
        when(patientRepository.findBySsn(ssn)).thenReturn(Optional.empty());

        // When
        boolean result = hospitalService.deleteAppointmentsBySSN(ssn);

        // Then
        assertFalse(result);
        verify(appointmentRepository, never()).deleteAll(any());
    }

    @Test
    void shouldFindLatestAppointment() {
        // Given
        String ssn = "123-45-6789";
        Appointment olderAppointment = new Appointment("Old", "2025-01-10", testPatient);
        olderAppointment.setId(2L);
        testPatient.setAppointments(Arrays.asList(testAppointment, olderAppointment));
        
        when(patientRepository.findBySsn(ssn)).thenReturn(Optional.of(testPatient));

        // When
        Optional<AppointmentDto> result = hospitalService.findLatestAppointmentBySSN(ssn);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testAppointment.getId(), result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenNoAppointments() {
        // Given
        String ssn = "123-45-6789";
        testPatient.setAppointments(Arrays.asList());
        when(patientRepository.findBySsn(ssn)).thenReturn(Optional.of(testPatient));

        // When
        Optional<AppointmentDto> result = hospitalService.findLatestAppointmentBySSN(ssn);

        // Then
        assertFalse(result.isPresent());
    }
} 