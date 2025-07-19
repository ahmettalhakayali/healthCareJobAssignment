package nl.gerimedica.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Simple integration tests for AppointmentController.
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class AppointmentControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldCreateBulkAppointments() throws Exception {
        // Given
        BulkAppointmentRequest request = new BulkAppointmentRequest();
        request.setReasons(Arrays.asList("Checkup"));
        request.setDates(Arrays.asList("2025-01-15"));

        // When & Then
        mockMvc.perform(post("/api/v1/appointments/bulk")
                .param("patientName", "John Doe")
                .param("ssn", "123-45-6789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnBadRequestForInvalidBulkRequest() throws Exception {
        // Given
        BulkAppointmentRequest request = new BulkAppointmentRequest();
        request.setReasons(Arrays.asList());
        request.setDates(Arrays.asList());

        // When & Then
        mockMvc.perform(post("/api/v1/appointments/bulk")
                .param("patientName", "John Doe")
                .param("ssn", "123-45-6789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSearchAppointmentsByReason() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/appointments/search")
                .param("keyword", "Checkup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnBadRequestForEmptySearchKeyword() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/appointments/search")
                .param("keyword", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteAppointmentsBySSN() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/appointments")
                .param("ssn", "123-45-6789"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestForEmptySSN() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/appointments")
                .param("ssn", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetLatestAppointment() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/appointments/latest")
                .param("ssn", "123-45-6789"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestForEmptySSNInLatest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/appointments/latest")
                .param("ssn", ""))
                .andExpect(status().isBadRequest());
    }
} 